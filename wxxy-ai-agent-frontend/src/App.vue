<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from './store'
import { userLogout } from './api'

const router = useRouter()
const userStore = useUserStore()

// 计算属性，检查用户是否登录
const isLoggedIn = computed(() => userStore.isLoggedIn)
const userAccount = computed(() => userStore.userInfo?.userAccount || '')

// 跳转到登录页面
const goToLogin = () => {
  router.push('/login')
}

// 处理退出登录
const handleLogout = async () => {
  try {
    await userLogout()
    userStore.clearUserInfo()
    userStore.saveToLocalStorage()
    // 退出登录后跳转到首页
    router.push('/')
  } catch (error) {
    console.error('退出登录失败:', error)
  }
}
</script>

<template>
  <div id="app">
    <!-- 顶部导航栏 -->
    <nav class="top-nav">
      <div class="nav-content">
        <div class="nav-title">
          <router-link to="/" class="app-logo">
            <span class="logo-text">AI智能助手平台</span>
          </router-link>
        </div>
        
        <div class="user-menu">
          <!-- 未登录时显示登录按钮 -->
          <button v-if="!isLoggedIn" class="login-button" @click="goToLogin">
            登录
          </button>
          
          <!-- 已登录时显示用户信息和下拉菜单 -->
          <div v-else class="user-dropdown">
            <button class="user-button">
              <span class="user-name">{{ userAccount }}</span>
              <span class="dropdown-icon">▼</span>
            </button>
            <div class="dropdown-menu">
              <button class="dropdown-item" @click="handleLogout">
                退出登录
              </button>
            </div>
          </div>
        </div>
      </div>
    </nav>
    
    <!-- 主内容区域 -->
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<style>
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

html, body {
  font-family: 'PingFang SC', 'Microsoft YaHei', 'Helvetica Neue', Helvetica, Arial, sans-serif;
  font-size: 16px;
  color: #333;
  background-color: #f0f2f5;
  width: 100%;
  height: 100%;
  overflow-x: hidden;
}

#app {
  width: 100%;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 顶部导航栏样式 */
.top-nav {
  background-color: rgba(13, 17, 23, 0.98);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 1000;
  backdrop-filter: blur(10px);
}

.nav-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px;
  height: 60px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.nav-title .app-logo {
  display: flex;
  align-items: center;
}

.logo-text {
  font-size: 20px;
  font-weight: 600;
  color: white;
  font-family: 'Orbitron', sans-serif;
  letter-spacing: 1px;
}

/* 用户菜单样式 */
.user-menu {
  position: relative;
}

.login-button {
  background: linear-gradient(90deg, #00b4ff, #0072ff);
  color: white;
  border: none;
  border-radius: 6px;
  padding: 10px 20px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.login-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0, 180, 255, 0.3);
}

/* 用户下拉菜单样式 */
.user-dropdown {
  position: relative;
}

.user-button {
  display: flex;
  align-items: center;
  gap: 8px;
  background: none;
  border: none;
  color: white;
  padding: 10px 15px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.2s;
}

.user-button:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.dropdown-icon {
  font-size: 12px;
  transition: transform 0.3s;
}

.user-dropdown:hover .dropdown-icon {
  transform: rotate(180deg);
}

.dropdown-menu {
  position: absolute;
  top: 100%;
  right: 0;
  margin-top: 5px;
  background-color: #1f2937;
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 6px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
  min-width: 120px;
  display: none;
  z-index: 1001;
}

.user-dropdown:hover .dropdown-menu {
  display: block;
}

.dropdown-item {
  display: block;
  width: 100%;
  padding: 10px 20px;
  background: none;
  border: none;
  color: white;
  text-align: left;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.2s;
}

.dropdown-item:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

/* 主内容区域样式 */
.main-content {
  flex: 1;
  width: 100%;
}

/* 响应式字体大小 */
@media (max-width: 768px) {
  html, body {
    font-size: 15px;
  }
  
  .nav-content {
    padding: 0 15px;
  }
  
  .logo-text {
    font-size: 18px;
  }
}

@media (max-width: 480px) {
  html, body {
    font-size: 14px;
  }
  
  .nav-content {
    padding: 0 10px;
  }
  
  .logo-text {
    font-size: 16px;
  }
}

/* 滚动条样式 */
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

::-webkit-scrollbar-thumb {
  background: #ccc;
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background: #aaa;
}
</style>
