import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { MarketTrend } from '@/api/types'
import { marketApi } from '@/api/market'

export const useMarketStore = defineStore('market', () => {
  const trends = ref<MarketTrend[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)

  async function fetchTrends() {
    loading.value = true
    error.value = null
    try {
      trends.value = await marketApi.trends()
    } catch (e) {
      error.value = '市場トレンドの取得に失敗しました'
      console.error(e)
    } finally {
      loading.value = false
    }
  }

  return { trends, loading, error, fetchTrends }
})
