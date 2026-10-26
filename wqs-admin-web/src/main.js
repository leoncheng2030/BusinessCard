import { createApp } from 'vue'
import { createPinia } from 'pinia'

import './style/index.less'
import wqs from './wqs'
import i18n from './locales'
import router from './router'
import App from './App.vue'
import './tailwind.css'

const app = createApp(App)
app.use(createPinia())
app.use(i18n)
app.use(wqs)
app.use(router)

// 挂载app
app.mount('#app')
