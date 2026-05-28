<template>
  <div class="flex flex-col items-center justify-center py-8 text-slate-400">
    <span class="text-4xl animate-float-slow">{{ emoji }}</span>
    <p class="mt-2 text-sm font-bold">{{ message }}</p>
  </div>
</template>

<script setup lang="ts">
withDefaults(
  defineProps<{ emoji?: string; message?: string }>(),
  { emoji: '🌷', message: '表示できる情報がありません' },
)
</script>
