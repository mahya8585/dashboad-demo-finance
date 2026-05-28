// バックエンド DTO と 1:1 対応する型定義

export interface Holding {
  productId: string
  productName: string
  assetClass: string
  amount: number
}

export interface Customer {
  id: string
  name: string
  avatarEmoji: string
  age: number
  riskTolerance: 'LOW' | 'MEDIUM' | 'HIGH' | string
  totalAssets: number
  investmentGoal: string
  holdings: Holding[]
}

export interface Product {
  id: string
  name: string
  category: string
  assetClass: string
  riskLevel: 'LOW' | 'MEDIUM' | 'HIGH' | string
  expectedReturn: number
  description: string
  tags: string[]
}

export type RecommendationBadge = 'similar' | 'trend' | 'risk-fit' | 'balance' | string

export interface Recommendation {
  product: Product
  totalScore: number
  confidence: number
  topReasons: string[]
  badges: RecommendationBadge[]
}

export interface RecentBuy {
  productId: string
  productName: string
  assetClass: string
  amount: number
  purchasedAt: string
}

export interface SimilarCustomer {
  id: string
  name: string
  avatarEmoji: string
  age: number
  riskTolerance: string
  similarityScore: number
  recentBuys: RecentBuy[]
}

export interface MarketTrend {
  id: string
  category: string
  headline: string
  summary: string
  indicator: string
  changePercent: number
  sparkline: number[]
  relatedTags: string[]
}

export interface RuleContribution {
  ruleName: string
  displayLabel: string
  weight: number
  score: number
  weightedScore: number
  narrative: string
}

export interface Explanation {
  product: Product
  finalScore: number
  ruleContributions: RuleContribution[]
  summaryNarrative: string
}
