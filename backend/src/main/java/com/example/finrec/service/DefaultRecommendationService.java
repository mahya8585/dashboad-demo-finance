package com.example.finrec.service;

import com.example.finrec.api.dto.CustomerDto;
import com.example.finrec.api.dto.ExplanationDto;
import com.example.finrec.api.dto.ProductDto;
import com.example.finrec.api.dto.RecommendationDto;
import com.example.finrec.api.dto.SimilarCustomerDto;
import com.example.finrec.engine.ExplanationBuilder;
import com.example.finrec.engine.MarketContext;
import com.example.finrec.engine.RecommendationEngine;
import com.example.finrec.engine.ScoredProduct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 推薦エンジン + 各サービスを組み合わせて Recommendation/Explanation を生成。
 * profile 非依存 — Dummy でも JPA でも同じ実装を使う。
 */
@Service
public class DefaultRecommendationService implements RecommendationService {

    private final CustomerService customerService;
    private final ProductService productService;
    private final SimilarCustomerService similarCustomerService;
    private final MarketTrendService marketTrendService;
    private final RecommendationEngine engine;
    private final ExplanationBuilder explanationBuilder;
    private final int topN;

    public DefaultRecommendationService(CustomerService customerService,
                                        ProductService productService,
                                        SimilarCustomerService similarCustomerService,
                                        MarketTrendService marketTrendService,
                                        RecommendationEngine engine,
                                        ExplanationBuilder explanationBuilder,
                                        @Value("${finrec.recommendation.top-n:6}") int topN) {
        this.customerService = customerService;
        this.productService = productService;
        this.similarCustomerService = similarCustomerService;
        this.marketTrendService = marketTrendService;
        this.engine = engine;
        this.explanationBuilder = explanationBuilder;
        this.topN = topN;
    }

    @Override
    public List<RecommendationDto> recommendFor(String customerId) {
        Optional<CustomerDto> opt = customerService.findById(customerId);
        if (opt.isEmpty()) {
            return List.of();
        }
        CustomerDto customer = opt.get();
        List<ProductDto> products = productService.listAll();
        List<SimilarCustomerDto> similar = similarCustomerService.findSimilar(customerId);
        MarketContext market = new MarketContext(marketTrendService.listTrends());
        return engine.recommendTopN(customer, products, similar, market, topN).stream()
                .map(explanationBuilder::toRecommendation)
                .toList();
    }

    @Override
    public Optional<ExplanationDto> explain(String productId, String customerId) {
        Optional<CustomerDto> custOpt = customerService.findById(customerId);
        Optional<ProductDto> prodOpt = productService.findById(productId);
        if (custOpt.isEmpty() || prodOpt.isEmpty()) {
            return Optional.empty();
        }
        List<SimilarCustomerDto> similar = similarCustomerService.findSimilar(customerId);
        MarketContext market = new MarketContext(marketTrendService.listTrends());
        ScoredProduct scored = engine.score(custOpt.get(), prodOpt.get(), similar, market);
        return Optional.of(explanationBuilder.toExplanation(scored));
    }
}
