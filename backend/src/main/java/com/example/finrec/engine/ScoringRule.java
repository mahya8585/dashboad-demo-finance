package com.example.finrec.engine;

import com.example.finrec.api.dto.CustomerDto;
import com.example.finrec.api.dto.ProductDto;
import com.example.finrec.api.dto.SimilarCustomerDto;

import java.util.List;

/**
 * 推薦スコアリングルールの抽象。
 */
public interface ScoringRule {

    /** 内部識別子（rule name）。テスト・I/F に露出する論理名。 */
    String name();

    /** XAI 表示用の日本語ラベル。 */
    String displayLabel();

    /** バッジ表示用の識別子（badge を出さないルールは空文字でも可）。 */
    String badge();

    /** ルールの重み（最終スコア = Σ weight × score）。 */
    double weight();

    /** 評価本体。 */
    RuleResult evaluate(CustomerDto customer,
                        ProductDto product,
                        List<SimilarCustomerDto> similarCustomers,
                        MarketContext market);
}
