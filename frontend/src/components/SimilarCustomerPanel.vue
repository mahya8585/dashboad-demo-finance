<template>
  <div class="kawaii-card p-5 h-full">
    <h2 class="text-base mb-3">👯 似ている顧客の動き</h2>
    <LoadingSpinner v-if="loading" />
    <EmptyState
      v-else-if="similar.length === 0"
      emoji="🌼"
      message="類似顧客の情報がありません"
    />
    <div v-else class="space-y-3">
      <div v-for="s in similar" :key="s.id" class="rounded-2xl bg-lavender-100/60 p-3">
        <div class="flex items-center gap-2.5 mb-2">
          <span class="text-2xl">{{ s.avatarEmoji }}</span>
          <div class="flex-1 min-w-0">
            <p class="font-bold text-sm truncate">{{ s.name }}</p>
            <p class="text-[11px] text-slate-500">
              {{ s.age }}歳・{{ riskLabel(s.riskTolerance) }}
            </p>
          </div>
          <span class="kawaii-chip">
            類似 {{ (s.similarityScore * 100).toFixed(0) }}%
          </span>
        </div>
        <p class="text-[11px] text-slate-500 mb-1">直近の購入</p>
        <ul class="space-y-1">
          <li
            v-for="b in s.recentBuys"
            :key="b.productId + b.purchasedAt"
            class="text-xs bg-white rounded-xl px-2.5 py-1.5 flex justify-between"
          >
            <span class="truncate mr-2">{{ b.productName }}</span>
            <span class="text-pinky-500 font-bold whitespace-nowrap">
              {{ formatYen(b.amount) }}
            </span>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { SimilarCustomer } from '@/api/types'
import { formatYen, riskLabel } from '@/utils/format'
import LoadingSpinner from './common/LoadingSpinner.vue'
import EmptyState from './common/EmptyState.vue'

defineProps<{ similar: SimilarCustomer[]; loading: boolean }>()
</script>
