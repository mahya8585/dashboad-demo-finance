package com.example.finrec.api.dto;

import java.math.BigDecimal;

public record RecentBuyDto(
        String productId,
        String productName,
        String assetClass,
        BigDecimal amount,
        String purchasedAt
) {
}
