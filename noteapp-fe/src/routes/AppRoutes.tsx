import { Navigate, Route, Routes, useLocation } from 'react-router-dom'
import type { ReactNode } from 'react'
import { AnimatePresence } from 'framer-motion'
import LoginPage from '../pages/login/LoginPage'
import RegisterPage from '../pages/register/RegisterPage'
import ForgotPasswordOtpPage from '../pages/forgot-password-Otp/ForgotPasswordPageOtp'
import ForgotPasswordPage from '../pages/forgot-password/ForgotPasswordPage'
import HomePage from '../pages/HomePage'

function PrivateRoute({ children }: { children: ReactNode }) {
  const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true'
  return isLoggedIn ? children : <Navigate to="/login" replace />
}

function AppRoutes() {
  const location = useLocation()

  return (
    <AnimatePresence mode="wait">
      <Routes location={location} key={location.pathname}>
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/forgot-password-otp" element={<ForgotPasswordOtpPage />} />
        <Route path="/forgot-password" element={<ForgotPasswordPage />} />
        <Route
          path="/home"
          element={
            <PrivateRoute>
              <HomePage />
            </PrivateRoute>
          }
        />
      </Routes>
    </AnimatePresence>
  )
}

export default AppRoutes