package com.example.finrec.service.dummy;

import com.example.finrec.api.dto.SimilarCustomerDto;
import com.example.finrec.service.SimilarCustomerService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("local")
public class DummySimilarCustomerService implements SimilarCustomerService {

    private final DummyDataStore store;

    public DummySimilarCustomerService(DummyDataStore store) {
        this.store = store;
    }

    @Override
    public List<SimilarCustomerDto> findSimilar(String customerId) {
        return store.similarFor(customerId);
    }
}
