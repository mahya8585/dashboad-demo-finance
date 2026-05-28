<template>
  <div class="kawaii-card p-4 flex flex-col gap-2 transition-transform hover:-translate-y-0.5">
    <div class="flex items-center justify-between">
      <span class="kawaii-chip">{{ trend.category }}</span>
      <span
        class="text-xs font-bold"
        :class="trend.changePercent >= 0 ? 'text-emerald-600' : 'text-rose-500'"
      >
        {{ trend.changePercent >= 0 ? '↑' : '↓' }} {{ formatPercent(trend.changePercent) }}
      </span>
    </div>
    <h3 class="text-sm leading-tight">{{ trend.headline }}</h3>
    <p class="text-[11px] text-slate-500 leading-snug">{{ trend.summary }}</p>

    <svg :viewBox="`0 0 ${width} ${height}`" class="w-full h-12">
      <polyline
        :points="points"
        fill="none"
        :stroke="trend.changePercent >= 0 ? '#5fd0a4' : '#ff7eb0'"
        stroke-width="2"
        stroke-linejoin="round"
        stroke-linecap="round"
      />
    </svg>
    <p class="text-[10px] text-slate-400">{{ trend.indicator }}</p>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { MarketTrend } from '@/api/types'
import { formatPercent } from '@/utils/format'

const props = defineProps<{ trend: MarketTrend }>()

const width = 160
const height = 40

const points = computed(() => {
  const series = props.trend.sparkline
  if (!series || series.length === 0) return ''
  const min = Math.min(...series)
  const max = Math.max(...series)
  const range = max - min || 1
  return series
    .map((v, i) => {
      const x = (i / (series.length - 1)) * width
      const y = height - ((v - min) / range) * height
      return `${x.toFixed(1)},${y.toFixed(1)}`
    })
    .join(' ')
})
</script>
