package com.example.finrec.engine;

import com.example.finrec.api.dto.CustomerDto;
import com.example.finrec.api.dto.HoldingDto;
import com.example.finrec.api.dto.ProductDto;
import com.example.finrec.api.dto.SimilarCustomerDto;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 全ルールを束ねた推薦エンジン本体。
 * Spring が List<ScoringRule> を全 Bean DI 経由で注入する。
 */
@Component
public class RecommendationEngine {

    private final List<ScoringRule> rules;

    public RecommendationEngine(List<ScoringRule> rules) {
        this.rules = List.copyOf(rules);
    }

    public List<ScoringRule> rules() {
        return rules;
    }

    /**
     * 1 商品の評価。
     */
    public ScoredProduct score(CustomerDto customer,
                               ProductDto product,
                               List<SimilarCustomerDto> similarCustomers,
                               MarketContext market) {
        Map<ScoringRule, RuleResult> results = new LinkedHashMap<>();
        double total = 0.0;
        for (ScoringRule rule : rules) {
            RuleResult r = rule.evaluate(customer, product, similarCustomers, market);
            results.put(rule, r);
            total += rule.weight() * r.score();
        }
        return new ScoredProduct(customer, product, total, results, similarCustomers);
    }

    /**
     * 顧客に対する全商品の Top N。保有済み商品は除外。
     */
    public List<ScoredProduct> recommendTopN(CustomerDto customer,
                                             List<ProductDto> products,
                                             List<SimilarCustomerDto> similarCustomers,
                                             MarketContext market,
                                             int topN) {
        Set<String> heldProductIds = customer.holdings().stream()
                .map(HoldingDto::productId)
                .collect(Collectors.toSet());
        return products.stream()
                .filter(p -> !heldProductIds.contains(p.id()))
                .map(p -> score(customer, p, similarCustomers, market))
                .sorted(Comparator.comparingDouble(ScoredProduct::totalScore).reversed())
                .limit(topN)
                .toList();
    }
}
