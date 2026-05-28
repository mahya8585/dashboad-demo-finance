package com.example.finrec.api.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductDto(
        String id,
        String name,
        String category,
        String assetClass,
        String riskLevel,
        BigDecimal expectedReturn,
        String description,
        List<String> tags
) {
}
