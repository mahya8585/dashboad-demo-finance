package com.example.finrec.engine;

import com.example.finrec.api.dto.CustomerDto;
import com.example.finrec.api.dto.HoldingDto;
import com.example.finrec.api.dto.MarketTrendDto;
import com.example.finrec.api.dto.ProductDto;
import com.example.finrec.api.dto.RecentBuyDto;
import com.example.finrec.api.dto.SimilarCustomerDto;
import com.example.finrec.engine.rule.AssetClassPreferenceRule;
import com.example.finrec.engine.rule.MarketTrendRule;
import com.example.finrec.engine.rule.RiskProfileMatchRule;
import com.example.finrec.engine.rule.SimilarCustomerRule;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RecommendationEngineTest {

    private final RecommendationEngine engine = new RecommendationEngine(List.of(
            new RiskProfileMatchRule(),
            new SimilarCustomerRule(),
            new MarketTrendRule(),
            new AssetClassPreferenceRule()
    ));

    private CustomerDto customer(String risk) {
        return new CustomerDto(
                "C001", "test", "🌸", 30, risk,
                new BigDecimal("1000000"), "growth",
                List.of(new HoldingDto("P_HELD", "held", "EQUITY", new BigDecimal("500000")))
        );
    }

    private ProductDto product(String id, String assetClass, String risk, List<String> tags) {
        return new ProductDto(id, "name-" + id, "fund", assetClass, risk,
                new BigDecimal("3.0"), "desc", tags);
    }

    private MarketContext rateUpMarket() {
        return new MarketContext(List.of(new MarketTrendDto(
                "T001", "rates", "金利上昇", "summary", "indicator", 0.5,
                List.of(1.0, 1.1), List.of("rate-up-friendly", "国内債券")
        )));
    }

    @Test
    void similarCustomerRule_boosts_scenario1_product() {
        CustomerDto c = customer("MEDIUM");
        ProductDto target = product("P_SIMILAR", "EQUITY", "MEDIUM",
                List.of("growth"));
        ProductDto other = product("P_OTHER", "EQUITY", "MEDIUM",
                List.of("growth"));

        SimilarCustomerDto sc = new SimilarCustomerDto(
                "S1", "sim", "🌷", 31, "MEDIUM", 0.9,
                List.of(new RecentBuyDto("P_SIMILAR", "name", "EQUITY",
                        new BigDecimal("100000"), "2026-05-01"))
        );

        ScoredProduct scoredTarget = engine.score(c, target, List.of(sc), new MarketContext(List.of()));
        ScoredProduct scoredOther = engine.score(c, other, List.of(sc), new MarketContext(List.of()));

        assertThat(scoredTarget.totalScore()).isGreaterThan(scoredOther.totalScore());
    }

    @Test
    void marketTrendRule_boosts_bond_in_rate_up_scenario2() {
        CustomerDto c = customer("LOW");
        ProductDto bond = product("P_BOND", "BOND", "LOW",
                List.of("国内債券", "rate-up-friendly"));
        ProductDto equity = product("P_EQ", "EQUITY", "LOW",
                List.of("growth"));

        ScoredProduct bondScored = engine.score(c, bond, List.of(), rateUpMarket());
        ScoredProduct eqScored = engine.score(c, equity, List.of(), rateUpMarket());

        assertThat(bondScored.totalScore()).isGreaterThan(eqScored.totalScore());
    }

    @Test
    void recommendTopN_excludes_already_held_products() {
        CustomerDto c = customer("MEDIUM");
        ProductDto held = product("P_HELD", "EQUITY", "MEDIUM", List.of("growth"));
        ProductDto fresh = product("P_NEW", "BOND", "MEDIUM", List.of("income"));

        List<ScoredProduct> top = engine.recommendTopN(c,
                List.of(held, fresh), List.of(), new MarketContext(List.of()), 5);

        assertThat(top).extracting(sp -> sp.product().id()).containsExactly("P_NEW");
    }

    @Test
    void confidence_is_between_zero_and_one() {
        CustomerDto c = customer("MEDIUM");
        ProductDto p = product("P_X", "BOND", "MEDIUM", List.of());
        ScoredProduct sp = engine.score(c, p, List.of(), new MarketContext(List.of()));
        assertThat(sp.confidence()).isBetween(0.0, 1.0);
    }
}
