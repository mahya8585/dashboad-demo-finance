package com.example.finrec.api.dto;

import java.math.BigDecimal;
import java.util.List;

public record CustomerDto(
        String id,
        String name,
        String avatarEmoji,
        int age,
        String riskTolerance,
        BigDecimal totalAssets,
        String investmentGoal,
        List<HoldingDto> holdings
) {
}
