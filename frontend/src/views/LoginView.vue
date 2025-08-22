<script>
import authService from '../services/authService.js'

export default {
  data() {
    return {
      email: '',
      password: '',
      error: '',
      loading: false
    }
  },
  async mounted() {
    // Check if user is already logged in and redirect to courses
    const isAuthenticated = await authService.checkAuthStatus()
    if (isAuthenticated) {
      this.$router.push('/courses')
    }
  },
  methods: {
    async login() {
      if (!this.email || !this.password) {
        this.error = 'Please fill in all fields'
        return
      }

      this.loading = true
      this.error = ''

      const result = await authService.login(this.email, this.password)
      
      if (result.success) {
        console.log('Login successful:', result.user)
        this.$router.push('/courses')
      } else {
        this.error = result.error
      }
      
      this.loading = false
    }
  }
}
</script>

<template>
    <div>
        <h1 class="text-success text-center">
            Please sign in
        </h1>
        <div class="container mt-5">
            <div class="row justify-content-center">
                <div class="col-md-6">
                    <div class="card">
                        <div class="card-body">
                            <div v-if="error" class="alert alert-danger">
                                {{ error }}
                            </div>
                            <form @submit.prevent="login">
                                <div class="form-group mb-3">
                                    <label for="email">
                                        Email
                                    </label>
                                    <input 
                                        v-model="email"
                                        type="email" 
                                        class="form-control" 
                                        id="email" 
                                        placeholder="Email" 
                                        required 
                                    />
                                </div>
                                <div class="form-group mb-3">
                                    <label for="password">
                                        Password
                                    </label>
                                    <input 
                                        v-model="password"
                                        type="password" 
                                        class="form-control" 
                                        id="password" 
                                        placeholder="Password"
                                        required 
                                    />
                                </div>
                                <button 
                                    type="submit" 
                                    class="btn btn-primary w-100"
                                    :disabled="loading"
                                >
                                    {{ loading ? 'Signing in...' : 'Login' }}
                                </button>
                            </form>
                            <p class="mt-3 text-center">
                                Not registered?
                                <router-link to="/register">Create an account</router-link>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>