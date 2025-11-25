import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createHead } from '@vueuse/head'
import { createPinia } from 'pinia'
import './style.css'
import { useUserStore } from './store'
import { getLoginUser } from './api'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

const app = createApp(App)
const head = createHead()
const pinia = createPinia()

app.use(router)
app.use(head)
app.use(pinia)
app.use(ElementPlus)

const userStore = useUserStore()

userStore.checkLoginState().finally(() => {
  app.mount('#app')
})
