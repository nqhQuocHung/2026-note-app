import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { apis, endpoints } from '../../config/apis'
import Loading from '../../components/loading/Loading'
import PageTransition from '../../components/transition/PageTransition'
import vnptLogo from '../../assets/logo/vnpt_logo.png'
import vnptBackground from '../../assets/logo/vnpt_bg.png'
import './Style.css'

function RegisterPage() {
  const navigate = useNavigate()

  const [formData, setFormData] = useState({
    username: '',
    password: '',
    confirmPassword: '',
    firstName: '',
    lastName: '',
    gender: '1',
    email: '',
    phone: '',
  })

  const [avatar, setAvatar] = useState<File | null>(null)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [loading, setLoading] = useState(false)

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
  ) => {
    const { name, value } = e.target

    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }))

    if (error) setError('')
    if (success) setSuccess('')
  }

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0] || null
    setAvatar(file)

    if (error) setError('')
    if (success) setSuccess('')
  }

  const validateForm = () => {
    if (!formData.username.trim()) {
      setError('Vui lòng nhập tài khoản')
      return false
    }

    if (!formData.password.trim()) {
      setError('Vui lòng nhập mật khẩu')
      return false
    }

    if (!formData.confirmPassword.trim()) {
      setError('Vui lòng nhập xác nhận mật khẩu')
      return false
    }

    if (formData.password !== formData.confirmPassword) {
      setError('Mật khẩu xác nhận không khớp')
      return false
    }

    if (!formData.lastName.trim()) {
      setError('Vui lòng nhập họ')
      return false
    }

    if (!formData.firstName.trim()) {
      setError('Vui lòng nhập tên')
      return false
    }

    if (!formData.email.trim()) {
      setError('Vui lòng nhập email')
      return false
    }

    if (!formData.phone.trim()) {
      setError('Vui lòng nhập số điện thoại')
      return false
    }

    return true
  }

  const handleRegister: React.FormEventHandler<HTMLFormElement> = async (e) => {
    e.preventDefault()
    setError('')
    setSuccess('')

    if (!validateForm()) return

    try {
      setLoading(true)

      const payload = new FormData()
      payload.append('username', formData.username.trim())
      payload.append('password', formData.password.trim())
      payload.append('firstName', formData.firstName.trim())
      payload.append('lastName', formData.lastName.trim())
      payload.append('gender', formData.gender)
      payload.append('email', formData.email.trim())
      payload.append('phone', formData.phone.trim())

      if (avatar) {
        payload.append('avatar', avatar)
      }

      await apis().post(endpoints.auth.register, payload, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      })

      setSuccess('Đăng ký tài khoản thành công')

      setTimeout(() => {
        navigate('/login')
      }, 1200)
    } catch (err: any) {
      setError(
        err?.response?.data?.message || 'Đăng ký thất bại, vui lòng thử lại',
      )
    } finally {
      setLoading(false)
    }
  }

  return (
    <PageTransition>
      {loading && <Loading fullScreen text="Đang đăng ký tài khoản..." />}

      <section
        className="register-page-bg register-page-shell"
        style={{ backgroundImage: `url(${vnptBackground})` }}
      >
        <div className="register-bg-overlay">
          <div className="register-container-fluid register-h-custom">
            <div className="register-row register-d-flex register-justify-content-center register-align-items-center register-h-100">
              <div className="register-col-md-8 register-col-lg-6 register-col-xl-4">
                <div className="register-scroll-area">
                  <form onSubmit={handleRegister} className="register-form-card">
                    <div className="register-logo-wrap">
                      <img
                        src={vnptLogo}
                        className="register-brand-image"
                        alt="VNPT Logo"
                      />
                    </div>

                    <div className="register-divider register-d-flex register-align-items-center register-my-4">
                      <p className="register-text-center register-fw-bold register-mx-3 register-mb-0">
                        Đăng ký tài khoản
                      </p>
                    </div>

                    {error && (
                      <div className="register-alert-danger">{error}</div>
                    )}
                    {success && (
                      <div className="register-alert-success">{success}</div>
                    )}

                    <div className="register-form-outline register-mb-3">
                      <label
                        className="register-form-label"
                        htmlFor="registerUsername"
                      >
                        Tài khoản
                      </label>
                      <input
                        type="text"
                        id="registerUsername"
                        name="username"
                        className="register-form-control register-form-control-lg"
                        placeholder="Nhập tài khoản"
                        value={formData.username}
                        onChange={handleChange}
                      />
                    </div>

                    <div className="register-form-row register-mb-3">
                      <div className="register-form-col">
                        <label
                          className="register-form-label"
                          htmlFor="registerPassword"
                        >
                          Mật khẩu
                        </label>
                        <input
                          type="password"
                          id="registerPassword"
                          name="password"
                          className="register-form-control register-form-control-lg"
                          placeholder="Nhập mật khẩu"
                          value={formData.password}
                          onChange={handleChange}
                        />
                      </div>

                      <div className="register-form-col">
                        <label
                          className="register-form-label"
                          htmlFor="registerConfirmPassword"
                        >
                          Xác nhận mật khẩu
                        </label>
                        <input
                          type="password"
                          id="registerConfirmPassword"
                          name="confirmPassword"
                          className="register-form-control register-form-control-lg"
                          placeholder="Nhập lại mật khẩu"
                          value={formData.confirmPassword}
                          onChange={handleChange}
                        />
                      </div>
                    </div>

                    <div className="register-form-row register-mb-3">
                      <div className="register-form-col">
                        <label
                          className="register-form-label"
                          htmlFor="registerLastName"
                        >
                          Họ
                        </label>
                        <input
                          type="text"
                          id="registerLastName"
                          name="lastName"
                          className="register-form-control register-form-control-lg"
                          placeholder="Nhập họ"
                          value={formData.lastName}
                          onChange={handleChange}
                        />
                      </div>

                      <div className="register-form-col">
                        <label
                          className="register-form-label"
                          htmlFor="registerFirstName"
                        >
                          Tên
                        </label>
                        <input
                          type="text"
                          id="registerFirstName"
                          name="firstName"
                          className="register-form-control register-form-control-lg"
                          placeholder="Nhập tên"
                          value={formData.firstName}
                          onChange={handleChange}
                        />
                      </div>
                    </div>

                    <div className="register-form-outline register-mb-3">
                      <label
                        className="register-form-label"
                        htmlFor="registerEmail"
                      >
                        Email
                      </label>
                      <input
                        type="email"
                        id="registerEmail"
                        name="email"
                        className="register-form-control register-form-control-lg"
                        placeholder="Nhập email"
                        value={formData.email}
                        onChange={handleChange}
                      />
                    </div>

                    <div className="register-form-row register-mb-3">
                      <div className="register-form-col register-form-col-sm">
                        <label
                          className="register-form-label"
                          htmlFor="registerGender"
                        >
                          Giới tính
                        </label>
                        <select
                          id="registerGender"
                          name="gender"
                          className="register-form-control register-form-control-lg"
                          value={formData.gender}
                          onChange={handleChange}
                        >
                          <option value="1">Nam</option>
                          <option value="2">Nữ</option>
                          <option value="0">Khác</option>
                        </select>
                      </div>

                      <div className="register-form-col register-form-col-lg">
                        <label
                          className="register-form-label"
                          htmlFor="registerPhone"
                        >
                          Số điện thoại
                        </label>
                        <input
                          type="text"
                          id="registerPhone"
                          name="phone"
                          className="register-form-control register-form-control-lg"
                          placeholder="Nhập số điện thoại"
                          value={formData.phone}
                          onChange={handleChange}
                        />
                      </div>
                    </div>

                    <div className="register-form-outline register-mb-3">
                      <label
                        className="register-form-label"
                        htmlFor="registerAvatar"
                      >
                        Ảnh đại diện
                      </label>
                      <input
                        type="file"
                        id="registerAvatar"
                        name="avatar"
                        accept="image/*"
                        className="register-form-control register-form-control-lg register-file-input"
                        onChange={handleFileChange}
                      />
                    </div>

                    <div className="register-form-options register-mb-3">
                      <button
                        type="button"
                        className="register-link-action"
                        onClick={() => navigate('/login')}
                      >
                        Quay lại đăng nhập
                      </button>
                    </div>

                    <div className="register-text-center register-mt-4 register-pt-2">
                      <button
                        type="submit"
                        className="register-btn register-btn-primary register-btn-lg"
                        disabled={loading}
                      >
                        {loading ? 'Đang đăng ký...' : 'Đăng ký'}
                      </button>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </PageTransition>
  )
}

export default RegisterPage