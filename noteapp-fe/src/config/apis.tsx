import axios from 'axios'

const BASE_URL = 'http://localhost:8080'

const endpoints = {
  auth: {
    login: '/api/v1/auth/login',
    register: '/api/v1/auth/register',
    refreshToken: '/api/v1/auth/refresh-token',
    forgotPassword: '/api/v1/auth/forgot-password',
    changePassword: '/api/v1/auth/change-password',
    otpForgotPassword: '/api/v1/auth/otp-forgot-password',
    otpChangePassword: '/api/v1/auth/otp-change-password',
    getUserById: (id: number | string) => `/api/v1/auth/user/${id}`,
    patchUserById: (id: number | string) => `/api/v1/auth/user/${id}`,
    putUserById: (id: number | string) => `/api/v1/auth/user/${id}`,
  },
}

const apis = (accessToken?: string) => {
  const instance = axios.create({
    baseURL: BASE_URL,
    headers: {
      'Content-Type': 'application/json',
      ...(accessToken ? { Authorization: `Bearer ${accessToken}` } : {}),
    },
  })

  instance.interceptors.response.use(
    (response) => response,
    (error) => {
      if (error.response) {
        console.error('Lỗi từ server:', error.response)
      } else if (error.request) {
        console.error('Không nhận được phản hồi từ server:', error.request)
      } else {
        console.error('Lỗi không xác định:', error.message)
      }
      return Promise.reject(error)
    },
  )

  return instance
}

export { apis, endpoints }