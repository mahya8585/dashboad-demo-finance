package com.example.finrec.service;

import com.example.finrec.api.dto.MarketTrendDto;

import java.util.List;

public interface MarketTrendService {
    List<MarketTrendDto> listTrends();
}
