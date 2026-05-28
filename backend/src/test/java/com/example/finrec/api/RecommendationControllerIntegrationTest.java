package com.example.finrec.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("local")
class RecommendationControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void getCustomers_returnsList() throws Exception {
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    void getRecommendations_returnsScoredItems() throws Exception {
        mockMvc.perform(get("/api/recommendations").param("customerId", "C001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].product.id").exists())
                .andExpect(jsonPath("$[0].totalScore").exists())
                .andExpect(jsonPath("$[0].topReasons").isArray());
    }

    @Test
    void getMarketTrends_returnsTrends() throws Exception {
        mockMvc.perform(get("/api/market-trends"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].headline").exists());
    }

    @Test
    void getExplanation_returnsContributions() throws Exception {
        mockMvc.perform(get("/api/explanations/P012").param("customerId", "C002"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product.id").value("P012"))
                .andExpect(jsonPath("$.ruleContributions[0].ruleName").exists());
    }
}
