import { api } from './client'
import type { MarketTrend } from './types'

export const marketApi = {
  trends: async (): Promise<MarketTrend[]> => {
    const { data } = await api.get<MarketTrend[]>('/api/market-trends')
    return data
  },
}
