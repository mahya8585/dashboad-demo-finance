/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{vue,js,ts}'],
  theme: {
    extend: {
      colors: {
        // パステルカラーパレット（かわいいデザイン用）
        pinky: {
          50: '#fff5f9',
          100: '#ffe8f1',
          200: '#ffc8df',
          300: '#ffa3c8',
          400: '#ff7eb0',
          500: '#ff5a99',
        },
        peach: {
          100: '#fff0e6',
          200: '#ffd9bf',
          300: '#ffb98f',
        },
        mint: {
          100: '#e6fff5',
          200: '#bff0dc',
          300: '#8fe0c0',
          400: '#5fd0a4',
        },
        lavender: {
          100: '#f3ecff',
          200: '#dcc8ff',
          300: '#c4a4ff',
        },
        cream: {
          50: '#fffdf5',
          100: '#fff9e6',
          200: '#fff0b8',
        },
        sky2: {
          100: '#e6f4ff',
          200: '#bfe3ff',
          300: '#8fcfff',
        },
      },
      fontFamily: {
        round: ['"M PLUS Rounded 1c"', '"Noto Sans JP"', 'sans-serif'],
        sans: ['"Noto Sans JP"', 'sans-serif'],
      },
      boxShadow: {
        soft: '0 6px 20px -8px rgba(255, 158, 196, 0.45)',
        pop: '0 10px 28px -10px rgba(196, 164, 255, 0.55)',
      },
      borderRadius: {
        '4xl': '2rem',
      },
      keyframes: {
        bounceSoft: {
          '0%, 100%': { transform: 'translateY(0)' },
          '50%': { transform: 'translateY(-6px)' },
        },
        floatSlow: {
          '0%, 100%': { transform: 'translateY(0)' },
          '50%': { transform: 'translateY(-4px)' },
        },
      },
      animation: {
        'bounce-soft': 'bounceSoft 1.4s ease-in-out infinite',
        'float-slow': 'floatSlow 3s ease-in-out infinite',
      },
    },
  },
  plugins: [],
}
