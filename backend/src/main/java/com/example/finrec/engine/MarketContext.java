package com.example.finrec.engine;

import com.example.finrec.api.dto.MarketTrendDto;

import java.util.List;

/**
 * 推薦エンジンに渡す共有コンテキスト。
 */
public record MarketContext(
        List<MarketTrendDto> trends
) {
}
