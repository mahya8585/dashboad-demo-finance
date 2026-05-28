package com.example.finrec.engine.rule;

import com.example.finrec.api.dto.CustomerDto;
import com.example.finrec.api.dto.ProductDto;
import com.example.finrec.api.dto.SimilarCustomerDto;
import com.example.finrec.engine.MarketContext;
import com.example.finrec.engine.RuleResult;
import com.example.finrec.engine.ScoringRule;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 顧客のリスク許容度と商品リスク階級の一致度を評価。
 */
@Component
public class RiskProfileMatchRule implements ScoringRule {

    private static final Map<String, Integer> RISK_RANK = Map.of(
            "LOW", 1,
            "MEDIUM", 2,
            "HIGH", 3
    );

    @Override
    public String name() {
        return "RiskProfileMatch";
    }

    @Override
    public String displayLabel() {
        return "リスク許容度との一致";
    }

    @Override
    public String badge() {
        return "risk-fit";
    }

    @Override
    public double weight() {
        return 0.35;
    }

    @Override
    public RuleResult evaluate(CustomerDto customer,
                               ProductDto product,
                               List<SimilarCustomerDto> similarCustomers,
                               MarketContext market) {
        int cr = RISK_RANK.getOrDefault(customer.riskTolerance(), 2);
        int pr = RISK_RANK.getOrDefault(product.riskLevel(), 2);
        int diff = Math.abs(cr - pr);
        double score = switch (diff) {
            case 0 -> 1.0;
            case 1 -> 0.6;
            default -> 0.2;
        };
        boolean triggered = diff == 0;
        String narrative = diff == 0
                ? String.format("%sの%s許容度に商品リスク(%s)が完全一致しています。",
                customer.name(), customer.riskTolerance(), product.riskLevel())
                : String.format("リスク許容度(%s)と商品リスク(%s)のずれは%d段階です。",
                customer.riskTolerance(), product.riskLevel(), diff);
        return new RuleResult(score, narrative, triggered);
    }
}
