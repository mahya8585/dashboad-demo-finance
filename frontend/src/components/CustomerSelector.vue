<template>
  <div class="relative">
    <label class="text-xs font-bold text-pinky-500 ml-2">🌷 顧客を選ぶ</label>
    <select
      :value="modelValue ?? ''"
      @change="onChange"
      class="block w-full mt-1 rounded-full bg-white border-2 border-pinky-200 px-4 py-2 pr-9
             font-bold text-slate-700 shadow-soft focus:outline-none focus:border-pinky-400
             cursor-pointer"
    >
      <option v-for="c in customers" :key="c.id" :value="c.id">
        {{ c.avatarEmoji }} {{ c.name }}（{{ c.age }}歳・{{ riskLabel(c.riskTolerance) }}）
      </option>
    </select>
  </div>
</template>

<script setup lang="ts">
import type { Customer } from '@/api/types'
import { riskLabel } from '@/utils/format'

defineProps<{
  customers: Customer[]
  modelValue: string | null
}>()
const emit = defineEmits<{ (e: 'update:modelValue', id: string): void }>()

function onChange(ev: Event) {
  const v = (ev.target as HTMLSelectElement).value
  emit('update:modelValue', v)
}
</script>
