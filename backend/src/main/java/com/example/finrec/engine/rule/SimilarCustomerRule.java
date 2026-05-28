package com.example.finrec.engine.rule;

import com.example.finrec.api.dto.CustomerDto;
import com.example.finrec.api.dto.ProductDto;
import com.example.finrec.api.dto.RecentBuyDto;
import com.example.finrec.api.dto.SimilarCustomerDto;
import com.example.finrec.engine.MarketContext;
import com.example.finrec.engine.RuleResult;
import com.example.finrec.engine.ScoringRule;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 類似顧客が直近購入した商品をブースト。
 * シナリオ① 類似顧客が購入 → この銘柄おすすめ
 */
@Component
public class SimilarCustomerRule implements ScoringRule {

    @Override
    public String name() {
        return "SimilarCustomer";
    }

    @Override
    public String displayLabel() {
        return "類似顧客の購買傾向";
    }

    @Override
    public String badge() {
        return "similar";
    }

    @Override
    public double weight() {
        return 0.30;
    }

    @Override
    public RuleResult evaluate(CustomerDto customer,
                               ProductDto product,
                               List<SimilarCustomerDto> similarCustomers,
                               MarketContext market) {
        int matchCount = 0;
        double similarityAcc = 0.0;
        for (SimilarCustomerDto sc : similarCustomers) {
            for (RecentBuyDto rb : sc.recentBuys()) {
                if (rb.productId().equals(product.id())) {
                    matchCount++;
                    similarityAcc += sc.similarityScore();
                }
            }
        }
        if (matchCount == 0) {
            return RuleResult.zero("類似顧客の直近購入には含まれていません。");
        }
        double avgSim = similarityAcc / matchCount;
        double score = Math.min(1.0, 0.5 + 0.5 * avgSim);
        String narrative = String.format("類似度の高い顧客%d名が直近この銘柄を購入(平均類似度 %.2f)。", matchCount, avgSim);
        return new RuleResult(score, narrative, true);
    }
}
