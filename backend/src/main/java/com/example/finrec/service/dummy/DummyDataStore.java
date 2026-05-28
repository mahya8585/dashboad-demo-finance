package com.example.finrec.service.dummy;

import com.example.finrec.api.dto.CustomerDto;
import com.example.finrec.api.dto.MarketTrendDto;
import com.example.finrec.api.dto.ProductDto;
import com.example.finrec.api.dto.SimilarCustomerDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * ローカル検証時のみ有効化される、JSONベースのインメモリストア。
 */
@Component
@Profile("local")
public class DummyDataStore {

    private static final Logger log = LoggerFactory.getLogger(DummyDataStore.class);

    private final ObjectMapper objectMapper;

    private List<CustomerDto> customers = Collections.emptyList();
    private List<ProductDto> products = Collections.emptyList();
    private Map<String, List<SimilarCustomerDto>> similarCustomersByCustomerId = Collections.emptyMap();
    private List<MarketTrendDto> marketTrends = Collections.emptyList();

    public DummyDataStore(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void load() throws IOException {
        customers = readList("dummy-data/customers.json", new TypeReference<>() {
        });
        products = readList("dummy-data/products.json", new TypeReference<>() {
        });
        similarCustomersByCustomerId = readMap("dummy-data/similar-customers.json",
                new TypeReference<>() {
                });
        marketTrends = readList("dummy-data/market-trends.json", new TypeReference<>() {
        });
        log.info("DummyDataStore loaded: customers={}, products={}, similarKeys={}, trends={}",
                customers.size(), products.size(), similarCustomersByCustomerId.size(),
                marketTrends.size());
    }

    private <T> List<T> readList(String path, TypeReference<List<T>> type) throws IOException {
        try (InputStream is = new ClassPathResource(path).getInputStream()) {
            return objectMapper.readValue(is, type);
        }
    }

    private <V> Map<String, V> readMap(String path, TypeReference<Map<String, V>> type)
            throws IOException {
        try (InputStream is = new ClassPathResource(path).getInputStream()) {
            return objectMapper.readValue(is, type);
        }
    }

    public List<CustomerDto> customers() {
        return customers;
    }

    public List<ProductDto> products() {
        return products;
    }

    public List<SimilarCustomerDto> similarFor(String customerId) {
        return similarCustomersByCustomerId.getOrDefault(customerId, Collections.emptyList());
    }

    public List<MarketTrendDto> marketTrends() {
        return marketTrends;
    }
}
