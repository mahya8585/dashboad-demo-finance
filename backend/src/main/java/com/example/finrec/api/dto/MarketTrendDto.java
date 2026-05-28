package com.example.finrec.api.dto;

import java.util.List;

public record MarketTrendDto(
        String id,
        String category,
        String headline,
        String summary,
        String indicator,
        double changePercent,
        List<Double> sparkline,
        List<String> relatedTags
) {
}
