package com.example.finrec.api.dto;

import java.util.List;

public record ExplanationDto(
        ProductDto product,
        double finalScore,
        List<RuleContributionDto> ruleContributions,
        String summaryNarrative
) {
}
