<template>
  <div class="kawaii-card p-5 flex flex-col gap-3 h-full">
    <div class="flex items-center gap-3">
      <div
        class="w-14 h-14 rounded-full bg-gradient-to-br from-pinky-100 to-lavender-100
               flex items-center justify-center text-3xl animate-float-slow"
      >
        {{ customer.avatarEmoji }}
      </div>
      <div>
        <h2 class="text-lg leading-tight">{{ customer.name }}</h2>
        <p class="text-xs text-slate-500">{{ customer.age }}歳 / 目標: {{ customer.investmentGoal }}</p>
      </div>
    </div>

    <div class="flex flex-wrap gap-2 mt-1">
      <span class="kawaii-badge" :class="riskColor(customer.riskTolerance)">
        🎯 {{ riskLabel(customer.riskTolerance) }}
      </span>
      <span class="kawaii-badge bg-cream-200 text-amber-700">
        💰 総資産 {{ formatYen(customer.totalAssets) }}
      </span>
    </div>

    <div class="mt-2">
      <p class="text-xs font-bold text-slate-500 mb-1.5">📦 保有商品</p>
      <div class="space-y-1.5">
        <div
          v-for="h in customer.holdings"
          :key="h.productId"
          class="flex items-center justify-between text-xs bg-pinky-50 rounded-2xl px-3 py-1.5"
        >
          <span class="font-bold truncate">{{ h.productName }}</span>
          <span class="text-pinky-500 font-bold">{{ formatYen(h.amount) }}</span>
        </div>
        <div v-if="customer.holdings.length === 0" class="text-xs text-slate-400">
          まだ保有商品はありません
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Customer } from '@/api/types'
import { formatYen, riskColor, riskLabel } from '@/utils/format'

defineProps<{ customer: Customer }>()
</script>
