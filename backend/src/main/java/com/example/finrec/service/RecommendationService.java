package com.example.finrec.service;

import com.example.finrec.api.dto.ExplanationDto;
import com.example.finrec.api.dto.RecommendationDto;

import java.util.List;
import java.util.Optional;

public interface RecommendationService {

    List<RecommendationDto> recommendFor(String customerId);

    Optional<ExplanationDto> explain(String productId, String customerId);
}
