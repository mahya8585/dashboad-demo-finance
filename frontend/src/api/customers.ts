import { api } from './client'
import type { Customer } from './types'

export const customersApi = {
  list: async (): Promise<Customer[]> => {
    const { data } = await api.get<Customer[]>('/api/customers')
    return data
  },
  get: async (id: string): Promise<Customer> => {
    const { data } = await api.get<Customer>(`/api/customers/${encodeURIComponent(id)}`)
    return data
  },
}
