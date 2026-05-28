# 🌷 投資商品レコメンドダッシュボード（デモ）

AI 活用の投資商品レコメンドを「かわいい UI」で見せるデモアプリです。

- **フロントエンド**: Vue 3 + TypeScript + Vite + TailwindCSS + Pinia
- **バックエンド**: Spring Boot 3.x + Java 21 + Gradle (Kotlin DSL)
- **DB**: PostgreSQL（`prod` プロファイルのみ。`local` プロファイルでは JSON ダミーデータ）

フロント / バックは別サーバで起動する疎結合構成です。

---

## ディレクトリ

```
.
├─ backend/    Spring Boot REST API (port 8080)
└─ frontend/   Vue 3 SPA (port 5173)
```

---

## 前提

- Java 21（`java -version` で確認）
- Node.js 18+ と npm
- 任意: PostgreSQL 15（prod プロファイルでのみ使用）

---

## 起動方法（ローカル検証 = DB なし、ダミーデータ）

### 1. バックエンド

```powershell
cd backend
./gradlew bootRun
```

- ポート: `http://localhost:8080`
- プロファイル: `local`（`application.yml` で `spring.profiles.active: local` を既定値に設定）
- ダミーデータは `backend/src/main/resources/dummy-data/*.json` からメモリにロード

#### 動作確認

```powershell
curl http://localhost:8080/api/customers
curl "http://localhost:8080/api/recommendations?customerId=C001"
curl "http://localhost:8080/api/explanations/P012?customerId=C002"
curl http://localhost:8080/api/market-trends
```

### 2. フロントエンド

別ターミナルで:

```powershell
cd frontend
npm install
npm run dev
```

- ポート: `http://localhost:5173`
- API ベース URL は `frontend/.env.development` の `VITE_API_BASE=http://localhost:8080` で指定
- バックエンド CORS は `localhost:5173` / `127.0.0.1:5173` を許可済み

ブラウザで `http://localhost:5173` を開くとダッシュボードが表示されます。

---

## API 一覧

| Method | Path                                              | 説明                            |
| ------ | ------------------------------------------------- | ------------------------------- |
| GET    | `/api/customers`                                  | ダミー顧客一覧                  |
| GET    | `/api/customers/{id}`                             | 顧客詳細                        |
| GET    | `/api/recommendations?customerId=`                | 推薦商品 Top N                  |
| GET    | `/api/similar-customers?customerId=`              | 類似顧客と直近購入               |
| GET    | `/api/market-trends`                              | 市場トレンド + sparkline         |
| GET    | `/api/explanations/{productId}?customerId=`       | Explainable AI：寄与度内訳      |

---

## テスト

```powershell
cd backend
./gradlew test
```

- `RecommendationEngineTest` — 4 ルール組み合わせの挙動（シナリオ① 類似顧客／シナリオ② 金利上昇など）
- `RecommendationControllerIntegrationTest` — MockMvc による API 統合テスト

---

## シナリオ確認のヒント

1. **シナリオ① 類似顧客が購入 → この銘柄おすすめ**
   - 顧客 `C001` を選択 → 類似顧客が直近購入した「米国S&P500ファンド」がおすすめ上位にバッジ `👯 類似顧客` 付きで表示されます。
2. **シナリオ② 金利上昇 → 債券商品提示**
   - 顧客 `C002`(LOW リスク) を選択 → 「金利上昇」トレンドにマッチする債券系商品が `📈 市場トレンド` バッジ付きで上位に並びます。
3. **Explainable AI**
   - 商品カードの「✨ 推薦理由をみる」をクリック → ルール別寄与度のバーと自然文 narrative が表示されます。

---

## 本番（prod プロファイル）でのデータベース利用

`local` 以外のプロファイル（例: `prod`）で起動すると、PostgreSQL に接続します（実装は今回スコープ外。`@Profile("prod")` の Bean を別途用意して切り替え可能な構成）。最小スキーマ例：

```sql
-- 雛形のみ（実 prod 動作確認はデモ範囲外）
CREATE TABLE customer (
    id            VARCHAR(20) PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    avatar_emoji  VARCHAR(8),
    age           INT,
    risk_tolerance VARCHAR(20),
    total_assets  NUMERIC(18, 2),
    investment_goal VARCHAR(200)
);

CREATE TABLE product (
    id            VARCHAR(20) PRIMARY KEY,
    name          VARCHAR(200) NOT NULL,
    category      VARCHAR(50),
    asset_class   VARCHAR(20),
    risk_level    VARCHAR(20),
    expected_return NUMERIC(6, 3),
    description   TEXT,
    tags          TEXT[]
);

CREATE TABLE holding (
    customer_id VARCHAR(20) REFERENCES customer(id),
    product_id  VARCHAR(20) REFERENCES product(id),
    amount      NUMERIC(18, 2),
    PRIMARY KEY (customer_id, product_id)
);

CREATE TABLE similar_link (
    customer_id    VARCHAR(20) REFERENCES customer(id),
    similar_id     VARCHAR(20),
    similarity     NUMERIC(4, 3)
);

CREATE TABLE market_trend (
    id          VARCHAR(20) PRIMARY KEY,
    category    VARCHAR(40),
    headline    VARCHAR(200),
    summary     TEXT,
    indicator   VARCHAR(80),
    change_percent NUMERIC(6, 3),
    sparkline   NUMERIC(12, 4)[],
    related_tags TEXT[]
);
```

起動例:

```powershell
$env:SPRING_PROFILES_ACTIVE = "prod"
$env:DB_URL = "jdbc:postgresql://localhost:5432/finrec"
$env:DB_USER = "finrec"
$env:DB_PASSWORD = "finrec"
cd backend
./gradlew bootRun
```

> ⚠️ prod プロファイル用の `@Profile("prod")` 実装クラス（`service/impl/db/`）は本デモには未同梱です。
> 上記スキーマと併せて、`JpaCustomerService` 等を実装することで切替できます。

---

## 設計ポイント

- **疎結合**: フロント/バックは独立サーバ。API 契約は REST + JSON。CORS で許可 origin を限定。
- **ダミー切替**: `@Profile("local")` の Bean (`Dummy*Service`) が JSON データを返却。本番想定では `@Profile("prod")` の JPA 実装に差替。
- **Explainable AI**: `ScoringRule` 抽象 × 4 実装ルール（リスク適合 / 類似顧客 / 市場トレンド / 分散）。`ExplanationBuilder` が寄与度と自然文 narrative を組み立て。
- **将来 LLM/ML 差替**: `ScoringRule` interface を実装する新ルール、または `RecommendationEngine` 自体を差し替えることで段階的に高度化可能。
