package com.example.finrec.engine;

import com.example.finrec.api.dto.ExplanationDto;
import com.example.finrec.api.dto.RecommendationDto;
import com.example.finrec.api.dto.RuleContributionDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * ScoredProduct を Recommendation / Explanation DTO に変換するヘルパ。
 */
@Component
public class ExplanationBuilder {

    public RecommendationDto toRecommendation(ScoredProduct scored) {
        List<Map.Entry<ScoringRule, RuleResult>> sorted = scored.ruleResults().entrySet().stream()
                .sorted(Comparator.comparingDouble(
                        (Map.Entry<ScoringRule, RuleResult> e) -> e.getKey().weight() * e.getValue().score()
                ).reversed())
                .toList();

        List<String> topReasons = new ArrayList<>();
        List<String> badges = new ArrayList<>();
        for (Map.Entry<ScoringRule, RuleResult> e : sorted) {
            RuleResult r = e.getValue();
            if (r.triggered()) {
                if (topReasons.size() < 3) {
                    topReasons.add(r.narrative());
                }
                String b = e.getKey().badge();
                if (b != null && !b.isBlank() && !badges.contains(b)) {
                    badges.add(b);
                }
            }
        }
        if (topReasons.isEmpty() && !sorted.isEmpty()) {
            topReasons.add(sorted.get(0).getValue().narrative());
        }

        return new RecommendationDto(
                scored.product(),
                round(scored.totalScore()),
                round(scored.confidence()),
                topReasons,
                badges
        );
    }

    public ExplanationDto toExplanation(ScoredProduct scored) {
        List<RuleContributionDto> contributions = new ArrayList<>();
        StringBuilder summary = new StringBuilder();
        summary.append(scored.customer().name()).append("さんへの「")
                .append(scored.product().name()).append("」の推薦理由：");
        boolean appendedAny = false;
        for (Map.Entry<ScoringRule, RuleResult> e : scored.ruleResults().entrySet()) {
            ScoringRule rule = e.getKey();
            RuleResult res = e.getValue();
            double weighted = rule.weight() * res.score();
            contributions.add(new RuleContributionDto(
                    rule.name(),
                    rule.displayLabel(),
                    round(rule.weight()),
                    round(res.score()),
                    round(weighted),
                    res.narrative()
            ));
            if (res.triggered() && !appendedAny) {
                summary.append(res.narrative());
                appendedAny = true;
            }
        }
        contributions.sort(Comparator.comparingDouble(RuleContributionDto::weightedScore).reversed());
        if (!appendedAny) {
            summary.append("総合スコアに基づく相対順位での推薦です。");
        }

        return new ExplanationDto(
                scored.product(),
                round(scored.totalScore()),
                contributions,
                summary.toString()
        );
    }

    private static double round(double v) {
        return Math.round(v * 1000.0) / 1000.0;
    }
}
