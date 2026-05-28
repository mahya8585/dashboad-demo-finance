package com.example.finrec.api.dto;

public record RuleContributionDto(
        String ruleName,
        String displayLabel,
        double weight,
        double score,
        double weightedScore,
        String narrative
) {
}
