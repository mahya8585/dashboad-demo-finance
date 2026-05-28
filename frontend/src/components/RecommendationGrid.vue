<template>
  <div class="kawaii-card p-5">
    <div class="flex items-center justify-between mb-3">
      <h2 class="text-base">🌟 あなたへのおすすめ商品</h2>
      <span class="text-xs text-slate-400">{{ recommendations.length }}件</span>
    </div>

    <LoadingSpinner v-if="loading" />
    <EmptyState
      v-else-if="recommendations.length === 0"
      emoji="🌸"
      message="おすすめ商品がまだありません"
    />
    <div v-else class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-3">
      <RecommendationCard
        v-for="rec in recommendations"
        :key="rec.product.id"
        :recommendation="rec"
        @explain="(id) => $emit('explain', id)"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Recommendation } from '@/api/types'
import RecommendationCard from './RecommendationCard.vue'
import LoadingSpinner from './common/LoadingSpinner.vue'
import EmptyState from './common/EmptyState.vue'

defineProps<{
  recommendations: Recommendation[]
  loading: boolean
}>()
defineEmits<{ (e: 'explain', productId: string): void }>()
</script>
