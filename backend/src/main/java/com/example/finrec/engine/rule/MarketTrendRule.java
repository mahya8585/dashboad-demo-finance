package com.example.finrec.engine.rule;

import com.example.finrec.api.dto.CustomerDto;
import com.example.finrec.api.dto.MarketTrendDto;
import com.example.finrec.api.dto.ProductDto;
import com.example.finrec.api.dto.SimilarCustomerDto;
import com.example.finrec.engine.MarketContext;
import com.example.finrec.engine.RuleResult;
import com.example.finrec.engine.ScoringRule;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 市場トレンドのタグと商品タグの一致でブースト。
 * シナリオ② 金利上昇 → 債券商品提示
 */
@Component
public class MarketTrendRule implements ScoringRule {

    @Override
    public String name() {
        return "MarketTrend";
    }

    @Override
    public String displayLabel() {
        return "市場トレンドとの整合";
    }

    @Override
    public String badge() {
        return "trend";
    }

    @Override
    public double weight() {
        return 0.25;
    }

    @Override
    public RuleResult evaluate(CustomerDto customer,
                               ProductDto product,
                               List<SimilarCustomerDto> similarCustomers,
                               MarketContext market) {
        Set<String> productTags = new HashSet<>(product.tags());
        List<String> matchedHeadlines = new ArrayList<>();
        int totalMatchTags = 0;
        double magnitudeAcc = 0.0;

        for (MarketTrendDto trend : market.trends()) {
            int hits = 0;
            for (String tag : trend.relatedTags()) {
                if (productTags.contains(tag)) {
                    hits++;
                }
            }
            if (hits > 0) {
                totalMatchTags += hits;
                magnitudeAcc += Math.min(1.0, Math.abs(trend.changePercent()) / 3.0);
                matchedHeadlines.add(trend.headline());
            }
        }

        if (totalMatchTags == 0) {
            return RuleResult.zero("現在の市場トレンドとは関連が薄い商品です。");
        }
        double score = Math.min(1.0, 0.4 + 0.2 * totalMatchTags + 0.2 * magnitudeAcc);
        String narrative = "市場トレンド「" + String.join("／", matchedHeadlines)
                + "」と商品の特性が整合しています。";
        return new RuleResult(score, narrative, true);
    }
}
