package com.example.finrec.service.dummy;

import com.example.finrec.api.dto.MarketTrendDto;
import com.example.finrec.service.MarketTrendService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("local")
public class DummyMarketTrendService implements MarketTrendService {

    private final DummyDataStore store;

    public DummyMarketTrendService(DummyDataStore store) {
        this.store = store;
    }

    @Override
    public List<MarketTrendDto> listTrends() {
        return store.marketTrends();
    }
}
