package com.example.finrec.api.dto;

import java.math.BigDecimal;

public record HoldingDto(
        String productId,
        String productName,
        String assetClass,
        BigDecimal amount
) {
}
