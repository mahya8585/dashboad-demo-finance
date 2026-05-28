package com.example.finrec.service;

import com.example.finrec.api.dto.SimilarCustomerDto;

import java.util.List;

public interface SimilarCustomerService {
    List<SimilarCustomerDto> findSimilar(String customerId);
}
