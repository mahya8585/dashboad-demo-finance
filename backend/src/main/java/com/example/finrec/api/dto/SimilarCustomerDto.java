package com.example.finrec.api.dto;

import java.util.List;

public record SimilarCustomerDto(
        String id,
        String name,
        String avatarEmoji,
        int age,
        String riskTolerance,
        double similarityScore,
        List<RecentBuyDto> recentBuys
) {
}
