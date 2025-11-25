import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../store'
import Home from '../views/Home.vue'
import LoveMaster from '../views/LoveMaster.vue'
import SuperAgent from '../views/SuperAgent.vue'
import LoginRegister from '../views/LoginRegister.vue'


const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    meta: {
      title: 'AI智能助手平台',
      requiresAuth: false
    }
  },
  {
    path: '/login',
    name: 'LoginRegister',
    component: LoginRegister,
    meta: {
      title: '登录/注册',
      requiresAuth: false
    }
  },
  {
    path: '/love-master',
    name: 'LoveMaster',
    component: LoveMaster,
    meta: {
      title: 'Java面试官',
      requiresAuth: true
    }
  },
  {
    path: '/super-agent',
    name: 'SuperAgent',
    component: SuperAgent,
    meta: {
      title: 'AI超级智能体',
      requiresAuth: true
    }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 全局路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  if (to.meta.title) {
    document.title = to.meta.title
  }
  
  // 检查是否需要登录
  if (to.meta.requiresAuth) {
    const userStore = useUserStore()
    // 检查用户是否已登录
    if (!userStore.isLoggedIn) {
      // 未登录时，显示提示并跳转到登录页面
      // 不能直接用 ElMessage，恢复 alert 或通过 query 传递消息
      // alert('请先登录平台再进行访问')
      next({ name: 'LoginRegister', query: { msg: '请先登录平台再进行访问' } })
    } else {
      // 已登录，继续访问
      next()
    }
  } else {
    // 不需要登录的页面，直接访问
    next()
  }
})

export default router
