<script>
import authService from '../services/authService.js'

export default {
  async mounted() {
    // Check if user is already logged in and redirect to courses
    const isAuthenticated = await authService.checkAuthStatus()
    if (isAuthenticated) {
      this.$router.push('/courses')
    }
  },
  methods: {
    goToLogin() {
      this.$router.push('/login')
    },
    goToRegister() {
      this.$router.push('/register')
    }
  }
}
</script>

<template>
  <div class="centered-content">
    <h1>Welcome to the Language Learning Platform</h1>
    
    <div class="auth-buttons mt-3">
      <button @click="goToLogin" class="btn btn-primary me-2">Login</button>
      <button @click="goToRegister" class="btn btn-secondary">Register</button>
    </div>
  </div>
</template>

<style scoped>
.centered-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 80vh;
  text-align: center;
}
</style>
