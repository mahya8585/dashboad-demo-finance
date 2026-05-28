package com.example.finrec.service.dummy;

import com.example.finrec.api.dto.ProductDto;
import com.example.finrec.service.ProductService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Profile("local")
public class DummyProductService implements ProductService {

    private final DummyDataStore store;

    public DummyProductService(DummyDataStore store) {
        this.store = store;
    }

    @Override
    public List<ProductDto> listAll() {
        return store.products();
    }

    @Override
    public Optional<ProductDto> findById(String id) {
        return store.products().stream().filter(p -> p.id().equals(id)).findFirst();
    }
}
