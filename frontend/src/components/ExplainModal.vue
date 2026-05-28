<template>
  <transition name="fade">
    <div
      v-if="show"
      class="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/40 backdrop-blur-sm"
      @click.self="$emit('close')"
    >
      <div
        class="kawaii-card max-w-2xl w-full p-6 max-h-[85vh] overflow-y-auto"
        role="dialog"
        aria-modal="true"
      >
        <div class="flex items-start justify-between mb-3">
          <div>
            <p class="text-[11px] text-slate-400 font-bold">✨ 推薦理由 (Explainable AI)</p>
            <h2 class="text-lg leading-tight">{{ title }}</h2>
          </div>
          <button
            class="rounded-full w-8 h-8 bg-pinky-100 text-pinky-500 font-bold hover:bg-pinky-200"
            @click="$emit('close')"
            aria-label="閉じる"
          >×</button>
        </div>

        <LoadingSpinner v-if="loading" label="解析中..." />

        <template v-else-if="explanation">
          <p
            class="rounded-2xl bg-cream-100 px-4 py-3 text-sm text-slate-700 leading-relaxed mb-4"
          >
            🐰 {{ explanation.summaryNarrative }}
          </p>

          <div class="mb-3 flex items-center justify-between">
            <span class="text-sm font-bold text-slate-600">ルール別寄与度</span>
            <span class="text-xs text-purple-600 font-bold">
              総合スコア {{ (explanation.finalScore * 100).toFixed(1) }} pt
            </span>
          </div>

          <div class="space-y-3">
            <div
              v-for="rc in explanation.ruleContributions"
              :key="rc.ruleName"
              class="rounded-2xl bg-white border border-pinky-100 p-3"
            >
              <div class="flex items-center justify-between mb-1">
                <span class="font-bold text-sm">{{ rc.displayLabel }}</span>
                <span class="text-xs text-slate-500">
                  重み {{ (rc.weight * 100).toFixed(0) }}% × スコア
                  {{ (rc.score * 100).toFixed(0) }} =
                  <span class="font-bold text-purple-600">
                    {{ (rc.weightedScore * 100).toFixed(1) }}
                  </span>
                </span>
              </div>
              <div class="w-full h-2.5 bg-pinky-50 rounded-full overflow-hidden">
                <div
                  class="h-full bg-gradient-to-r from-pinky-300 to-lavender-300 transition-all"
                  :style="{ width: barWidth(rc.weightedScore) + '%' }"
                ></div>
              </div>
              <p class="mt-2 text-[12px] text-slate-600 leading-snug">{{ rc.narrative }}</p>
            </div>
          </div>
        </template>

        <EmptyState v-else emoji="🥲" message="推薦理由が取得できませんでした" />
      </div>
    </div>
  </transition>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { Explanation } from '@/api/types'
import LoadingSpinner from './common/LoadingSpinner.vue'
import EmptyState from './common/EmptyState.vue'

const props = defineProps<{
  show: boolean
  loading: boolean
  explanation: Explanation | null
}>()
defineEmits<{ (e: 'close'): void }>()

const title = computed(() =>
  props.explanation ? props.explanation.product.name : '推薦理由',
)

// 最大寄与度に対する相対バー幅 (最低 5% は表示)
function barWidth(weighted: number): number {
  if (!props.explanation) return 0
  const max = Math.max(
    ...props.explanation.ruleContributions.map((r) => Math.abs(r.weightedScore)),
    0.001,
  )
  const ratio = Math.abs(weighted) / max
  return Math.max(5, Math.round(ratio * 100))
}
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.18s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
