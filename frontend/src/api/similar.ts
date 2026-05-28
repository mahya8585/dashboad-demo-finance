import { api } from './client'
import type { SimilarCustomer } from './types'

export const similarApi = {
  list: async (customerId: string): Promise<SimilarCustomer[]> => {
    const { data } = await api.get<SimilarCustomer[]>('/api/similar-customers', {
      params: { customerId },
    })
    return data
  },
}
