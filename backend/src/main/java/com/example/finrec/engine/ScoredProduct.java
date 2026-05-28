package com.example.finrec.engine;

import com.example.finrec.api.dto.CustomerDto;
import com.example.finrec.api.dto.ProductDto;
import com.example.finrec.api.dto.SimilarCustomerDto;

import java.util.List;
import java.util.Map;

/**
 * 1 商品 × 1 顧客の評価結果。
 */
public record ScoredProduct(
        CustomerDto customer,
        ProductDto product,
        double totalScore,
        Map<ScoringRule, RuleResult> ruleResults,
        List<SimilarCustomerDto> similarCustomers
) {

    /** confidence: 全ルールの triggered 比率を 0..1 で。 */
    public double confidence() {
        if (ruleResults.isEmpty()) {
            return 0.0;
        }
        long triggered = ruleResults.values().stream().filter(RuleResult::triggered).count();
        return (double) triggered / ruleResults.size();
    }
}
