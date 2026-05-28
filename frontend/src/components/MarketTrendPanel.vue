<template>
  <div class="kawaii-card p-5">
    <div class="flex items-center justify-between mb-3">
      <h2 class="text-base">📊 主要市場トレンド</h2>
      <span class="text-xs text-slate-400">{{ trends.length }}件</span>
    </div>
    <LoadingSpinner v-if="loading" />
    <EmptyState v-else-if="trends.length === 0" emoji="🌧️" message="市場トレンドを取得できませんでした" />
    <div v-else class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-3">
      <MarketTrendCard v-for="t in trends" :key="t.id" :trend="t" />
    </div>
  </div>
</template>

<script setup lang="ts">
import type { MarketTrend } from '@/api/types'
import MarketTrendCard from './MarketTrendCard.vue'
import LoadingSpinner from './common/LoadingSpinner.vue'
import EmptyState from './common/EmptyState.vue'

defineProps<{ trends: MarketTrend[]; loading: boolean }>()
</script>
