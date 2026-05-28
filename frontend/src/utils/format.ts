export function formatYen(n: number): string {
  if (n === undefined || n === null || Number.isNaN(n)) return '-'
  if (n >= 100_000_000) return `${(n / 100_000_000).toFixed(2)}億円`
  if (n >= 10_000) return `${(n / 10_000).toFixed(0)}万円`
  return `${n.toLocaleString('ja-JP')}円`
}

export function formatPercent(n: number, fractionDigits = 1): string {
  if (n === undefined || n === null || Number.isNaN(n)) return '-'
  const sign = n > 0 ? '+' : ''
  return `${sign}${n.toFixed(fractionDigits)}%`
}

export function scoreToStars(score: number): string {
  // 0..1 score → 1..5 stars (visual)
  const stars = Math.max(1, Math.min(5, Math.round(score * 5)))
  return '★'.repeat(stars) + '☆'.repeat(5 - stars)
}

export function riskColor(level: string): string {
  switch (level) {
    case 'LOW':
      return 'bg-mint-200 text-emerald-700'
    case 'MEDIUM':
      return 'bg-cream-200 text-amber-700'
    case 'HIGH':
      return 'bg-pinky-200 text-rose-600'
    default:
      return 'bg-slate-100 text-slate-600'
  }
}

export function riskLabel(level: string): string {
  switch (level) {
    case 'LOW':
      return '低リスク'
    case 'MEDIUM':
      return '中リスク'
    case 'HIGH':
      return '高リスク'
    default:
      return level
  }
}

export interface BadgeStyle {
  label: string
  emoji: string
  cls: string
}

export function badgeStyle(badge: string): BadgeStyle {
  switch (badge) {
    case 'similar':
      return { label: '類似顧客', emoji: '👯', cls: 'bg-lavender-200 text-purple-700' }
    case 'trend':
      return { label: '市場トレンド', emoji: '📈', cls: 'bg-mint-200 text-emerald-700' }
    case 'risk-fit':
      return { label: 'リスク適合', emoji: '🎯', cls: 'bg-pinky-200 text-rose-600' }
    case 'balance':
      return { label: '分散効果', emoji: '🌷', cls: 'bg-sky2-200 text-sky-700' }
    default:
      return { label: badge, emoji: '✨', cls: 'bg-slate-100 text-slate-600' }
  }
}
