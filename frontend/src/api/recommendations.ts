import { api } from './client'
import type { Recommendation, Explanation } from './types'

export const recommendationsApi = {
  list: async (customerId: string): Promise<Recommendation[]> => {
    const { data } = await api.get<Recommendation[]>('/api/recommendations', {
      params: { customerId },
    })
    return data
  },
  explain: async (productId: string, customerId: string): Promise<Explanation> => {
    const { data } = await api.get<Explanation>(
      `/api/explanations/${encodeURIComponent(productId)}`,
      { params: { customerId } },
    )
    return data
  },
}
