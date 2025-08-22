<script>
import authService from '../services/authService.js'

export default {
  data() {
    return {
      user: null,
      loading: false
    }
  },
  async mounted() {
    await this.checkAuthStatus()
  },
  methods: {
    async checkAuthStatus() {
      await authService.checkAuthStatus()
      this.user = authService.getUser()
    },
    async logout() {
      this.loading = true
      const success = await authService.logout()
      if (success) {
        this.user = null
        this.$router.push('/')
      }
      this.loading = false
    }
  }
}
</script>

<template>
<nav class="navbar navbar-expand-lg bg-body-tertiary">
      <div class="container-fluid">
        <router-link to="/" class="navbar-brand">Language Learning Platform</router-link>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav me-auto">
            <li class="nav-item">
              <router-link to="/" class="nav-link">Home</router-link>
            </li>
            <li class="nav-item" v-if="user">
              <router-link to="/courses" class="nav-link">Browse Courses</router-link>
            </li>
          </ul>
          <ul class="navbar-nav">
            <li class="nav-item" v-if="!user">
              <router-link to="/login" class="nav-link">Login</router-link>
            </li>
            <li class="nav-item" v-if="!user">
              <router-link to="/register" class="nav-link">Register</router-link>
            </li>
            <li class="nav-item dropdown" v-if="user">
              <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                {{ user.name }} {{ user.surname }}
              </a>
              <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                <li><span class="dropdown-item-text">{{ user.email }}</span></li>
                <li><hr class="dropdown-divider"></li>
                <li>
                  <button 
                    class="dropdown-item" 
                    @click="logout"
                    :disabled="loading"
                  >
                    {{ loading ? 'Logging out...' : 'Logout' }}
                  </button>
                </li>
              </ul>
            </li>
          </ul>
        </div>
      </div>
    </nav>
</template>

<style scoped>
.router-link-active {
  font-weight: bold !important;
}

.router-link-exact-active {
  font-weight: bold !important;
}
</style>
