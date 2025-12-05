import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia' // Pinia 임포트

const app = createApp(App)

app.use(createPinia()) // [핵심] Vue 앱에 Pinia를 등록
app.use(router)

app.mount('#app')