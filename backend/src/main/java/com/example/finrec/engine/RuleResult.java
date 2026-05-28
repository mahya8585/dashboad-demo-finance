package com.example.finrec.engine;

/**
 * 1つのルールが1商品について返す評価結果。
 *
 * @param score     0.0〜1.0 のスコア
 * @param narrative 自然文での説明（XAI 用）
 * @param triggered ルールが実際にスコア寄与した（badge 表示などの判定に使用）
 */
public record RuleResult(double score, String narrative, boolean triggered) {

    public static RuleResult zero(String narrative) {
        return new RuleResult(0.0, narrative, false);
    }
}
