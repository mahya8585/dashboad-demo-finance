<template>
  <div
    class="kawaii-card p-4 flex flex-col gap-3 transition-transform hover:-translate-y-1 hover:shadow-pop"
  >
    <div class="flex items-start justify-between gap-2">
      <div>
        <p class="text-[11px] text-slate-400 font-bold">{{ recommendation.product.category }}</p>
        <h3 class="text-base leading-tight">{{ recommendation.product.name }}</h3>
      </div>
      <span class="kawaii-badge" :class="riskColor(recommendation.product.riskLevel)">
        {{ riskLabel(recommendation.product.riskLevel) }}
      </span>
    </div>

    <div class="flex items-center gap-3">
      <div
        class="rounded-2xl bg-gradient-to-br from-pinky-100 to-cream-100 px-3 py-2 text-center"
      >
        <p class="text-[10px] text-pinky-500 font-bold">おすすめ度</p>
        <p class="text-yellow-500 text-sm leading-none">{{ scoreToStars(recommendation.totalScore) }}</p>
      </div>
      <div class="text-xs text-slate-500 leading-tight flex-1">
        <p><span class="font-bold text-emerald-600">期待リターン:</span> {{ recommendation.product.expectedReturn.toFixed(1) }}%</p>
        <p class="mt-0.5">
          <span class="font-bold text-purple-600">確信度:</span>
          {{ (recommendation.confidence * 100).toFixed(0) }}%
        </p>
      </div>
    </div>

    <div class="flex flex-wrap gap-1.5">
      <span
        v-for="b in recommendation.badges"
        :key="b"
        class="kawaii-badge"
        :class="badgeStyle(b).cls"
      >
        {{ badgeStyle(b).emoji }} {{ badgeStyle(b).label }}
      </span>
    </div>

    <ul class="text-xs text-slate-600 space-y-1 leading-snug">
      <li v-for="(r, i) in recommendation.topReasons" :key="i" class="flex gap-1">
        <span class="text-pinky-400">✿</span><span>{{ r }}</span>
      </li>
    </ul>

    <button class="kawaii-btn text-xs mt-auto self-start" @click="$emit('explain', recommendation.product.id)">
      ✨ 推薦理由をみる
    </button>
  </div>
</template>

<script setup lang="ts">
import type { Recommendation } from '@/api/types'
import { badgeStyle, riskColor, riskLabel, scoreToStars } from '@/utils/format'

defineProps<{ recommendation: Recommendation }>()
defineEmits<{ (e: 'explain', productId: string): void }>()
</script>
