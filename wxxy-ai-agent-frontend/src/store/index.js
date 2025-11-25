import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

import { getLoginUser } from '../api'

export const useUserStore = defineStore('user', () => {
  // 状态
  const userInfo = ref(null)
  const isLoggedIn = ref(false)
  
  // 计算属性
  const getUserInfo = computed(() => userInfo.value)
  const getIsLoggedIn = computed(() => isLoggedIn.value)
  
  // 方法
  const setUserInfo = (userData) => {
    userInfo.value = userData
    isLoggedIn.value = true
  }
  
  const clearUserInfo = () => {
    userInfo.value = null
    isLoggedIn.value = false
  }
  
  // 持久化存储
  const saveToLocalStorage = () => {
    if (userInfo.value) {
      localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
      localStorage.setItem('isLoggedIn', 'true')
    }
  }
  
  const loadFromLocalStorage = () => {
    const savedUserInfo = localStorage.getItem('userInfo')
    const savedIsLoggedIn = localStorage.getItem('isLoggedIn')
    
    if (savedUserInfo && savedIsLoggedIn === 'true') {
      try {
        userInfo.value = JSON.parse(savedUserInfo)
        isLoggedIn.value = true
      } catch (error) {
        console.error('加载用户信息失败:', error)
        clearUserInfo()
      }
    }
  }

  // 新增：实时校验后端 session 登录态
  const checkLoginState = async () => {
    try {
      const user = await getLoginUser()
      if (user) {
        setUserInfo(user)
        saveToLocalStorage()
      } else {
        clearUserInfo()
        localStorage.removeItem('userInfo')
        localStorage.removeItem('isLoggedIn')
      }
    } catch (e) {
      clearUserInfo()
      localStorage.removeItem('userInfo')
      localStorage.removeItem('isLoggedIn')
    }
  }
  
  return {
    userInfo,
    isLoggedIn,
    getUserInfo,
    getIsLoggedIn,
    setUserInfo,
    clearUserInfo,
    saveToLocalStorage,
    loadFromLocalStorage,
    checkLoginState
  }
})
