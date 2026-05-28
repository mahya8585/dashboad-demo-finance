import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Recommendation, SimilarCustomer, Explanation } from '@/api/types'
import { recommendationsApi } from '@/api/recommendations'
import { similarApi } from '@/api/similar'

export const useRecommendationStore = defineStore('recommendation', () => {
  const recommendations = ref<Recommendation[]>([])
  const similar = ref<SimilarCustomer[]>([])
  const explanation = ref<Explanation | null>(null)

  const loadingRecs = ref(false)
  const loadingSimilar = ref(false)
  const loadingExplanation = ref(false)
  const error = ref<string | null>(null)

  async function fetchFor(customerId: string) {
    error.value = null
    loadingRecs.value = true
    loadingSimilar.value = true
    try {
      const [recs, sim] = await Promise.all([
        recommendationsApi.list(customerId),
        similarApi.list(customerId),
      ])
      recommendations.value = recs
      similar.value = sim
    } catch (e) {
      error.value = 'おすすめ情報の取得に失敗しました'
      console.error(e)
    } finally {
      loadingRecs.value = false
      loadingSimilar.value = false
    }
  }

  async function fetchExplanation(productId: string, customerId: string) {
    loadingExplanation.value = true
    try {
      explanation.value = await recommendationsApi.explain(productId, customerId)
    } catch (e) {
      console.error(e)
      explanation.value = null
    } finally {
      loadingExplanation.value = false
    }
  }

  function clearExplanation() {
    explanation.value = null
  }

  return {
    recommendations,
    similar,
    explanation,
    loadingRecs,
    loadingSimilar,
    loadingExplanation,
    error,
    fetchFor,
    fetchExplanation,
    clearExplanation,
  }
})
