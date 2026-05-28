<template>
  <div class="min-h-screen">
    <!-- Header -->
    <header class="px-4 md:px-8 pt-6 pb-4">
      <div class="flex flex-col md:flex-row md:items-end md:justify-between gap-4">
        <div>
          <h1 class="text-2xl md:text-3xl">🌷 おすすめ投資ダッシュボード</h1>
          <p class="text-xs md:text-sm text-slate-500 mt-1">
            AI があなたの“今いちばん良い”投資先をふんわりご提案します ♡
          </p>
        </div>
        <div class="w-full md:w-80">
          <CustomerSelector
            :customers="customerStore.customers"
            :model-value="customerStore.selectedId"
            @update:model-value="onSelectCustomer"
          />
        </div>
      </div>
    </header>

    <!-- Banner errors -->
    <div v-if="anyError" class="px-4 md:px-8">
      <div class="kawaii-card border-pinky-300 bg-pinky-50 px-4 py-3 text-sm text-rose-500">
        {{ anyError }}
      </div>
    </div>

    <!-- Main grid -->
    <main class="px-4 md:px-8 pb-10 grid grid-cols-1 xl:grid-cols-12 gap-4 mt-2">
      <!-- Top row: profile + similar -->
      <section class="xl:col-span-4">
        <LoadingSpinner v-if="customerStore.loading && !customerStore.selected" label="顧客情報を取得中…" />
        <CustomerProfileCard
          v-else-if="customerStore.selected"
          :customer="customerStore.selected"
        />
      </section>

      <section class="xl:col-span-4">
        <SimilarCustomerPanel
          :similar="recommendationStore.similar"
          :loading="recommendationStore.loadingSimilar"
        />
      </section>

      <section class="xl:col-span-4 kawaii-card p-5">
        <h2 class="text-base mb-2">🌈 今日のひとこと</h2>
        <p class="text-sm text-slate-600 leading-relaxed">
          市場トレンドと類似顧客の動きを踏まえて、
          <span class="font-bold text-pinky-500">{{ topPickLine }}</span>
        </p>
        <ul class="mt-3 space-y-1 text-xs text-slate-500">
          <li>📊 トレンド: {{ marketStore.trends.length }} 件</li>
          <li>👯 類似顧客: {{ recommendationStore.similar.length }} 名</li>
          <li>🌟 おすすめ商品: {{ recommendationStore.recommendations.length }} 件</li>
        </ul>
      </section>

      <!-- Recommendations -->
      <section class="xl:col-span-12">
        <RecommendationGrid
          :recommendations="recommendationStore.recommendations"
          :loading="recommendationStore.loadingRecs"
          @explain="onExplain"
        />
      </section>

      <!-- Market trends -->
      <section class="xl:col-span-12">
        <MarketTrendPanel
          :trends="marketStore.trends"
          :loading="marketStore.loading"
        />
      </section>
    </main>

    <ExplainModal
      :show="explainModalOpen"
      :loading="recommendationStore.loadingExplanation"
      :explanation="recommendationStore.explanation"
      @close="closeExplain"
    />
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { useCustomerStore } from '@/stores/customer'
import { useRecommendationStore } from '@/stores/recommendation'
import { useMarketStore } from '@/stores/market'
import CustomerSelector from '@/components/CustomerSelector.vue'
import CustomerProfileCard from '@/components/CustomerProfileCard.vue'
import RecommendationGrid from '@/components/RecommendationGrid.vue'
import SimilarCustomerPanel from '@/components/SimilarCustomerPanel.vue'
import MarketTrendPanel from '@/components/MarketTrendPanel.vue'
import ExplainModal from '@/components/ExplainModal.vue'
import LoadingSpinner from '@/components/common/LoadingSpinner.vue'

const customerStore = useCustomerStore()
const recommendationStore = useRecommendationStore()
const marketStore = useMarketStore()

const explainModalOpen = ref(false)

const anyError = computed(
  () =>
    customerStore.error ||
    recommendationStore.error ||
    marketStore.error ||
    null,
)

const topPickLine = computed(() => {
  const first = recommendationStore.recommendations[0]
  if (!first) return '商品を読み込んでいます…'
  return `${first.product.name} をいちばんに推せそうです♪`
})

onMounted(async () => {
  await Promise.all([customerStore.fetchAll(), marketStore.fetchTrends()])
  if (customerStore.selectedId) {
    await recommendationStore.fetchFor(customerStore.selectedId)
  }
})

watch(
  () => customerStore.selectedId,
  async (id) => {
    if (id) await recommendationStore.fetchFor(id)
  },
)

function onSelectCustomer(id: string) {
  customerStore.select(id)
}

async function onExplain(productId: string) {
  if (!customerStore.selectedId) return
  explainModalOpen.value = true
  await recommendationStore.fetchExplanation(productId, customerStore.selectedId)
}

function closeExplain() {
  explainModalOpen.value = false
  recommendationStore.clearExplanation()
}
</script>
