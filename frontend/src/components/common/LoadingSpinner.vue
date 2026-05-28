<template>
  <div class="flex items-center gap-1.5 text-pinky-400 text-sm">
    <span class="kawaii-dot bg-pinky-300 animate-bounce-soft" style="animation-delay: 0s"></span>
    <span class="kawaii-dot bg-lavender-300 animate-bounce-soft" style="animation-delay: 0.15s"></span>
    <span class="kawaii-dot bg-mint-300 animate-bounce-soft" style="animation-delay: 0.3s"></span>
    <span class="ml-1 font-bold">{{ label }}</span>
  </div>
</template>

<script setup lang="ts">
withDefaults(defineProps<{ label?: string }>(), { label: '読み込み中…' })
</script>
