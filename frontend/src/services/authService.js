import axios from 'axios'

// Configure axios defaults
axios.defaults.withCredentials = true

class AuthService {
  constructor() {
    this.user = null
    this.isLoading = false
    this.listeners = []
  }

  // Add a listener for user state changes
  addUserChangeListener(callback) {
    this.listeners.push(callback)
  }

  // Remove a listener
  removeUserChangeListener(callback) {
    this.listeners = this.listeners.filter(listener => listener !== callback)
  }

  // Notify all listeners when user state changes
  notifyUserChange() {
    this.listeners.forEach(callback => callback(this.user))
  }

  async checkAuthStatus() {
    try {
      const response = await axios.get('http://localhost:8080/api/auth/me')
      this.user = response.data
      this.notifyUserChange()
      return true
    } catch (error) {
      console.error('Auth check failed:', error)
      this.user = null
      this.notifyUserChange()
      return false
    }
  }

  async login(email, password) {
    try {
      const response = await axios.post('http://localhost:8080/api/auth/login', {
        email,
        password
      })
      this.user = response.data
      this.notifyUserChange()
      return { success: true, user: response.data }
    } catch (error) {
      const message = error.response?.data || 'Login failed. Please try again.'
      return { success: false, error: message }
    }
  }

  async register(userData) {
    try {
      const response = await axios.post('http://localhost:8080/api/auth/register', userData)
      this.user = response.data
      this.notifyUserChange()
      return { success: true, user: response.data }
    } catch (error) {
      const message = error.response?.data || 'Registration failed. Please try again.'
      return { success: false, error: message }
    }
  }

  async logout() {
    this.isLoading = true
    try {
      await axios.post('http://localhost:8080/api/auth/logout')
      this.user = null
      this.notifyUserChange()
      return true
    } catch (error) {
      console.error('Logout failed:', error)
      return false
    } finally {
      this.isLoading = false
    }
  }

  getUser() {
    return this.user
  }

  isAuthenticated() {
    return !!this.user
  }

  isUserLoading() {
    return this.isLoading
  }
}

// Export a singleton instance
export default new AuthService()
