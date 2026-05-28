package com.example.finrec.api.dto;

import java.util.List;

public record RecommendationDto(
        ProductDto product,
        double totalScore,
        double confidence,
        List<String> topReasons,
        List<String> badges
) {
}
