package com.example.finrec.service;

import com.example.finrec.api.dto.ProductDto;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductDto> listAll();

    Optional<ProductDto> findById(String id);
}
