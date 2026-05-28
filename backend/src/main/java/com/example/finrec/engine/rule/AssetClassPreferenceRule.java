package com.example.finrec.engine.rule;

import com.example.finrec.api.dto.CustomerDto;
import com.example.finrec.api.dto.HoldingDto;
import com.example.finrec.api.dto.ProductDto;
import com.example.finrec.api.dto.SimilarCustomerDto;
import com.example.finrec.engine.MarketContext;
import com.example.finrec.engine.RuleResult;
import com.example.finrec.engine.ScoringRule;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 顧客のポートフォリオ偏りを補正する。
 * 既に多く保有している資産クラスはスコアダウン、
 * 未保有または比率の低い資産クラスはスコアアップ。
 */
@Component
public class AssetClassPreferenceRule implements ScoringRule {

    @Override
    public String name() {
        return "AssetClassBalance";
    }

    @Override
    public String displayLabel() {
        return "ポートフォリオの分散";
    }

    @Override
    public String badge() {
        return "balance";
    }

    @Override
    public double weight() {
        return 0.10;
    }

    @Override
    public RuleResult evaluate(CustomerDto customer,
                               ProductDto product,
                               List<SimilarCustomerDto> similarCustomers,
                               MarketContext market) {
        Map<String, BigDecimal> byAsset = new HashMap<>();
        BigDecimal total = BigDecimal.ZERO;
        for (HoldingDto h : customer.holdings()) {
            byAsset.merge(h.assetClass(), h.amount(), BigDecimal::add);
            total = total.add(h.amount());
        }
        if (total.signum() == 0) {
            return new RuleResult(0.6, "保有資産がないため新規組入候補です。", true);
        }
        BigDecimal existing = byAsset.getOrDefault(product.assetClass(), BigDecimal.ZERO);
        double ratio = existing.doubleValue() / total.doubleValue();
        double score;
        boolean triggered;
        String narrative;
        if (ratio == 0.0) {
            score = 0.9;
            triggered = true;
            narrative = String.format("%s資産は未保有のため、分散効果が見込めます。", product.assetClass());
        } else if (ratio < 0.3) {
            score = 0.7;
            triggered = true;
            narrative = String.format("%s資産の保有比率は%.0f%%。追加でも分散維持。", product.assetClass(), ratio * 100);
        } else if (ratio < 0.5) {
            score = 0.4;
            triggered = false;
            narrative = String.format("%s資産は既に%.0f%%。比率に注意。", product.assetClass(), ratio * 100);
        } else {
            score = 0.15;
            triggered = false;
            narrative = String.format("%s資産が%.0f%%と偏りあり。追加は要検討。", product.assetClass(), ratio * 100);
        }
        return new RuleResult(score, narrative, triggered);
    }
}
