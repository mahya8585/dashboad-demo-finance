package com.example.finrec.api;

import com.example.finrec.api.dto.ExplanationDto;
import com.example.finrec.service.RecommendationService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/explanations")
@Validated
public class ExplanationController {

    private final RecommendationService recommendationService;

    public ExplanationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ExplanationDto> explain(
            @PathVariable @NotBlank String productId,
            @RequestParam("customerId") @NotBlank String customerId) {
        return recommendationService.explain(productId, customerId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
