package com.example.finrec.service;

import com.example.finrec.api.dto.CustomerDto;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<CustomerDto> listAll();

    Optional<CustomerDto> findById(String id);
}
