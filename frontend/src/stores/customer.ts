import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { Customer } from '@/api/types'
import { customersApi } from '@/api/customers'

export const useCustomerStore = defineStore('customer', () => {
  const customers = ref<Customer[]>([])
  const selectedId = ref<string | null>(null)
  const loading = ref(false)
  const error = ref<string | null>(null)

  const selected = computed(() =>
    customers.value.find((c) => c.id === selectedId.value) ?? null,
  )

  async function fetchAll() {
    loading.value = true
    error.value = null
    try {
      customers.value = await customersApi.list()
      if (!selectedId.value && customers.value.length > 0) {
        selectedId.value = customers.value[0].id
      }
    } catch (e) {
      error.value = '顧客一覧の取得に失敗しました'
      console.error(e)
    } finally {
      loading.value = false
    }
  }

  function select(id: string) {
    selectedId.value = id
  }

  return { customers, selectedId, selected, loading, error, fetchAll, select }
})
