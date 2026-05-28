package com.example.finrec.service.dummy;

import com.example.finrec.api.dto.CustomerDto;
import com.example.finrec.service.CustomerService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Profile("local")
public class DummyCustomerService implements CustomerService {

    private final DummyDataStore store;

    public DummyCustomerService(DummyDataStore store) {
        this.store = store;
    }

    @Override
    public List<CustomerDto> listAll() {
        return store.customers();
    }

    @Override
    public Optional<CustomerDto> findById(String id) {
        return store.customers().stream().filter(c -> c.id().equals(id)).findFirst();
    }
}
