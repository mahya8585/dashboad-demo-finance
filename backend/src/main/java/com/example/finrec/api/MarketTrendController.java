package com.example.finrec.api;

import com.example.finrec.api.dto.MarketTrendDto;
import com.example.finrec.service.MarketTrendService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/market-trends")
public class MarketTrendController {

    private final MarketTrendService marketTrendService;

    public MarketTrendController(MarketTrendService marketTrendService) {
        this.marketTrendService = marketTrendService;
    }

    @GetMapping
    public List<MarketTrendDto> list() {
        return marketTrendService.listTrends();
    }
}
