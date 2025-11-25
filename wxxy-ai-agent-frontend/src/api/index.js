import axios from 'axios'

// 根据环境变量设置 API 基础 URL
const API_BASE_URL = process.env.NODE_ENV === 'production' 
 ? '/api' // 生产环境使用相对路径，适用于前后端部署在同一域名下
 : 'http://localhost:8123/api' // 开发环境指向本地后端服务

// 创建axios实例
const request = axios.create({
  baseURL: API_BASE_URL,
  timeout: 60000,
  withCredentials: true // 允许跨域携带cookie
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    // 这里可以添加token等认证信息
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    // 处理响应数据
    if (response.data.code === 0) {
      return response.data.data
    } else {
      throw new Error(response.data.message || '请求失败')
    }
  },
  error => {
    // 统一错误处理
    console.error('请求错误:', error)
    throw error
  }
)

// 封装SSE连接
export const connectSSE = (url, params, onMessage, onError) => {
  // 构建带参数的URL
  const queryString = Object.keys(params)
    .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
    .join('&')
  
  const fullUrl = `${API_BASE_URL}${url}?${queryString}`
  
  // 创建EventSource
  const eventSource = new EventSource(fullUrl)
  
  eventSource.onmessage = event => {
    let data = event.data
    
    // 检查是否是特殊标记
    if (data === '[DONE]') {
      if (onMessage) onMessage('[DONE]')
    } else {
      // 处理普通消息
      if (onMessage) onMessage(data)
    }
  }
  
  eventSource.onerror = error => {
    if (onError) onError(error)
    eventSource.close()
  }
  
  // 返回eventSource实例，以便后续可以关闭连接
  return eventSource
}

// AI恋爱大师聊天
export const chatWithLoveApp = (message, chatId) => {
  return connectSSE('/ai/java_interview/chat/sse', { message, chatId })
}

// AI超级智能体聊天
export const chatWithManus = (message) => {
  return connectSSE('/ai/manus/chat', { message })
}

// 用户注册
export const userRegister = (userAccount, userPassword, checkPassword) => {
  return request.post('/user/register', {
    userAccount,
    userPassword,
    checkPassword
  })
}

// 用户登录
export const userLogin = (userAccount, userPassword) => {
  return request.post('/user/login', {
    userAccount,
    userPassword
  })
}

// 获取当前登录用户
export const getLoginUser = () => {
  return request.get('/user/get/login')
}

// 用户注销
export const userLogout = () => {
  return request.post('/user/logout')
}

export default {
  chatWithLoveApp,
  chatWithManus,
  userRegister,
  userLogin,
  getLoginUser,
  userLogout
}
