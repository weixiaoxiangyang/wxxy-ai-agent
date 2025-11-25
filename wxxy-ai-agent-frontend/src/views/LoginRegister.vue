<template>
  <div class="login-register-container">
    <div class="form-wrapper fade-in">
      <!-- 左上角返回按钮 -->
      <button class="back-button" @click="goHome">
        返回
      </button>
  <div class="form-header">
    <div class="platform-logo">
      <svg width="32" height="32" viewBox="0 0 32 32" fill="none">
        <circle cx="16" cy="16" r="16" fill="#0066ff"/>
        <text x="16" y="21" text-anchor="middle" font-size="16" fill="#fff" font-family="Arial">AI</text>
      </svg>
      <span class="platform-title">AI智能助手平台</span>
    </div>
    <h1>用户登录</h1>
  </div>
  
  <form @submit.prevent="handleSubmit" class="form">
    <div class="form-group">
      <span class="input-icon">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
          <path d="M12 12c2.7 0 5-2.3 5-5s-2.3-5-5-5-5 2.3-5 5 2.3 5 5 5zm0 2c-3.3 0-10 1.7-10 5v3h20v-3c0-3.3-6.7-5-10-5z" fill="#bbb"/>
        </svg>
      </span>
      <input
        v-model="formData.userAccount"
        type="text"
        placeholder="请输入账号"
        required
      />
    </div>
    
    <div class="form-group">
      <span class="input-icon">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
          <path d="M12 17a2 2 0 0 0 2-2v-3a2 2 0 0 0-4 0v3a2 2 0 0 0 2 2zm6-7V7a6 6 0 0 0-12 0v3a6 6 0 0 0 12 0z" fill="#bbb"/>
        </svg>
      </span>
      <input
        v-model="formData.userPassword"
        type="password"
        placeholder="请输入密码"
        required
      />
    </div>
    
    <div v-if="!isLogin" class="form-group">
      <span class="input-icon">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
          <path d="M12 17a2 2 0 0 0 2-2v-3a2 2 0 0 0-4 0v3a2 2 0 0 0 2 2zm6-7V7a6 6 0 0 0-12 0v3a6 6 0 0 0 12 0z" fill="#bbb"/>
        </svg>
      </span>
      <input
        v-model="formData.checkPassword"
        type="password"
        placeholder="请再次输入密码"
        required
      />
    </div>
    
    <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
    
    <button type="submit" class="submit-button" :disabled="isSubmitting">
      {{ isSubmitting ? '处理中...' : (isLogin ? '登录' : '注册') }}
    </button>
    
    <div class="register-link">
      <span>{{ isLogin ? '没有账号？' : '已有账号？' }}</span>
      <button type="button" class="toggle-button" @click="toggleForm">
        {{ isLogin ? '去注册' : '去登录' }}
      </button>
    </div>
  </form>
</div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../store'
import { userRegister, userLogin } from '../api'
import { ElMessage } from 'element-plus'
import { useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
// 返回首页
const goHome = () => {
  router.push('/')
}

// 监听路由 query.msg，若有则弹出 element-plus 警告
if (route.query.msg) {
  ElMessage.warning(route.query.msg)
}

const userStore = useUserStore()

// 表单状态
const isLogin = ref(true) // true表示登录表单，false表示注册表单
const isSubmitting = ref(false)
const errorMessage = ref('')

// 表单数据
const formData = ref({
  userAccount: '',
  userPassword: '',
  checkPassword: ''
})

// 切换登录/注册表单
const toggleForm = () => {
  isLogin.value = !isLogin.value
  errorMessage.value = ''
  // 清空表单数据
  formData.value = {
    userAccount: '',
    userPassword: '',
    checkPassword: ''
  }
}

// 处理表单提交
const handleSubmit = async () => {
  // 清除之前的错误信息
  errorMessage.value = ''
  
  // 表单验证
  if (!formData.value.userAccount.trim()) {
    errorMessage.value = '请输入账号'
    return
  }
  
  if (!formData.value.userPassword.trim()) {
    errorMessage.value = '请输入密码'
    return
  }
  
  // 注册表单需要验证两次密码是否一致
  if (!isLogin.value && formData.value.userPassword !== formData.value.checkPassword) {
    errorMessage.value = '两次输入的密码不一致'
    return
  }
  
  isSubmitting.value = true
  
  try {
    if (isLogin.value) {
      // 登录
      const userInfo = await userLogin(formData.value.userAccount, formData.value.userPassword)
      // 保存用户信息到store
      userStore.setUserInfo(userInfo)
      userStore.saveToLocalStorage()
      ElMessage.success('登录成功')
      // 跳转到首页
      router.push('/')
    } else {
      // 注册
      await userRegister(
        formData.value.userAccount,
        formData.value.userPassword,
        formData.value.checkPassword
      )
      ElMessage.success('注册成功，请登录')
      // 注册成功后切换到登录表单
      toggleForm()
    }
  } catch (error) {
    errorMessage.value = error.message || '操作失败，请重试'
    ElMessage.error(errorMessage.value)
    console.error('登录/注册失败:', error)
  } finally {
    isSubmitting.value = false
  }
}
</script>

<style scoped>
/* 左上角返回按钮样式 */
.back-button {
  position: absolute;
  top: 18px;
  left: 18px;
  background: #f5f5f5;
  border: 1px solid #ddd;
  border-radius: 6px;
  padding: 6px 16px;
  font-size: 15px;
  color: #333;
  cursor: pointer;
  z-index: 10;
  transition: background 0.2s, border 0.2s;
}
.back-button:hover {
  background: #e6f0ff;
  border-color: #0066ff;
  color: #0066ff;
}

/* 全局变量 */
:root {
  --primary-blue: #0066ff;
  --text-primary: #333333;
  --text-secondary: #666666;
  --border-color: #dddddd;
  --error-color: #ff4d4f;
}

.login-register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #18191A; /* 与home.vue一致的黑色背景 */
  position: relative;
  transition: background 0.3s;
}

.form-wrapper {
  background: rgba(255,255,255,0.92);
  border-radius: 16px;
  padding: 40px 32px 32px 32px;
  width: 100%;
  max-width: 400px;
  position: relative;
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.18);
  backdrop-filter: blur(4px);
  border: 1px solid rgba(255,255,255,0.18);
  transition: box-shadow 0.3s;
  animation: fadeIn 0.7s;
}
.fade-in {
  animation: fadeIn 0.7s;
}
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(30px);}
  to { opacity: 1; transform: translateY(0);}
}

.form-header {
  text-align: center;
  margin-bottom: 18px;
  position: relative;
}
.platform-logo {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 8px;
}
.platform-logo svg {
  margin-right: 8px;
}
.platform-title {
  font-size: 18px;
  font-weight: 700;
  color: #0066ff;
  letter-spacing: 1px;
}

.form-header h1 {
  color: #222;
  font-size: 28px;
  margin-bottom: 0;
  font-weight: 600;
  letter-spacing: 1px;
}

.form {
  display: flex;
  flex-direction: column;
}

.form-group {
  margin-bottom: 20px;
  position: relative;
  display: flex;
  align-items: center;
}
.input-icon {
  display: flex;
  align-items: center;
  margin-right: 8px;
  margin-left: 2px;
}

.form-group input {
  flex: 1;
  padding: 12px 16px;
  background: rgba(245,245,245,0.95);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  color: var(--text-primary);
  font-size: 16px;
  transition: border 0.3s, box-shadow 0.3s;
  box-sizing: border-box;
  outline: none;
}

.form-group input:focus {
  outline: none;
  border-color: #0066ff;
  box-shadow: 0 0 0 2px rgba(0, 102, 255, 0.18);
}

.form-group input::placeholder {
  color: #cccccc;
}

.error-message {
  color: var(--error-color);
  font-size: 14px;
  margin-bottom: 15px;
  text-align: center;
  padding: 8px 12px;
  background-color: #fff2f0;
  border-radius: 6px;
  border: 1px solid #ffccc7;
}

.submit-button {
  background: linear-gradient(90deg, #0066ff 0%, #3399ff 100%);
  color: #fff;
  border: none;
  border-radius: 8px;
  padding: 12px 20px;
  font-size: 17px;
  font-weight: 600;
  cursor: pointer;
  margin-top: 10px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0,102,255,0.08);
  transition: background 0.2s, box-shadow 0.2s;
}
.submit-button:hover:not(:disabled) {
  background: linear-gradient(90deg, #0050c8 0%, #3388ff 100%);
  box-shadow: 0 4px 16px rgba(0,102,255,0.12);
}

.submit-button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.register-link {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
  color: var(--text-secondary);
  font-size: 14px;
}

.toggle-button {
  background: none;
  border: none;
  color: var(--primary-blue);
  cursor: pointer;
  font-size: 14px;
  margin-left: 4px;
  padding: 0;
  font-weight: 600;
  text-decoration: underline;
  transition: color 0.2s;
}
.toggle-button:hover {
  color: #0050c8;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .form-wrapper {
    padding: 18px 4px 16px 4px;
    margin: 12px;
    max-width: 98vw;
  }
  .platform-title {
    font-size: 15px;
  }
  .form-header h1 {
    font-size: 22px;
  }
}
</style>
