package com.example.finrec.api;

import com.example.finrec.api.dto.SimilarCustomerDto;
import com.example.finrec.service.SimilarCustomerService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/similar-customers")
@Validated
public class SimilarCustomerController {

    private final SimilarCustomerService similarCustomerService;

    public SimilarCustomerController(SimilarCustomerService similarCustomerService) {
        this.similarCustomerService = similarCustomerService;
    }

    @GetMapping
    public List<SimilarCustomerDto> list(@RequestParam("customerId") @NotBlank String customerId) {
        return similarCustomerService.findSimilar(customerId);
    }
}
