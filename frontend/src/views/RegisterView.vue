<script>
import authService from '../services/authService.js'

export default {
  data() {
    return {
      name: '',
      surname: '',
      email: '',
      phoneNumber: '',
      password: '',
      repeatPassword: '',
      isTeacher: false,
      isStudent: false,
      error: '',
      loading: false
    }
  },
  mounted() {
    // Component mounted - route guard handles auth redirects
  },
  methods: {
    async register() {
      if (!this.name || !this.surname || !this.email || !this.password || !this.repeatPassword) {
        this.error = 'Please fill in all fields'
        return
      }

      if (this.password !== this.repeatPassword) {
        this.error = 'Passwords do not match'
        return
      }

      this.loading = true
      this.error = ''

      const userData = {
        name: this.name,
        surname: this.surname,
        email: this.email,
        password: this.password,
        phoneNumber: this.phoneNumber ? parseInt(this.phoneNumber) : null,
        isTeacher: this.isTeacher,
        isStudent: this.isStudent
      }

      const result = await authService.register(userData)
      
      if (result.success) {
        console.log('Registration successful:', result.user)
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
        <h1 class="text-success text-center">Create Account</h1>
        <div class="container mt-5">
            <div class="row justify-content-center">
                <div class="col-md-6">
                    <div class="card">
                        <div class="card-body">
                            <div v-if="error" class="alert alert-danger">
                                {{ error }}
                            </div>
                            <form @submit.prevent="register">
                                <div class="form-group mb-3">
                                    <label for="name">Name</label>
                                    <input 
                                        v-model="name"
                                        type="text" 
                                        class="form-control" 
                                        id="name" 
                                        placeholder="Name"
                                        required 
                                    />
                                </div>

                                <div class="form-group mb-3">
                                    <label for="surname">Surname</label>
                                    <input 
                                        v-model="surname"
                                        type="text" 
                                        class="form-control" 
                                        id="surname" 
                                        placeholder="Surname"
                                        required 
                                    />
                                </div>

                                <div class="form-group mb-3">
                                    <label for="email">Email</label>
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
                                    <label for="phoneNumber">Phone Number (optional)</label>
                                    <input 
                                        v-model="phoneNumber"
                                        type="number" 
                                        class="form-control" 
                                        id="phoneNumber" 
                                        placeholder="Phone Number"
                                    />
                                </div>
                                            
                                <div class="form-group mb-3">
                                    <label for="password">Password</label>
                                    <input 
                                        v-model="password"
                                        type="password" 
                                        class="form-control" 
                                        id="password" 
                                        placeholder="Password"
                                        required 
                                    />
                                </div>

                                <div class="form-group mb-3">
                                    <label for="repeatPassword">Repeat Password</label>
                                    <input 
                                        v-model="repeatPassword"
                                        type="password" 
                                        class="form-control" 
                                        id="repeatPassword" 
                                        placeholder="Repeat Password"
                                        required 
                                    />
                                </div>

                                <div class="form-check mb-3">
                                    <input 
                                        v-model="isTeacher"
                                        type="checkbox" 
                                        class="form-check-input" 
                                        id="isTeacher"
                                    />
                                    <label class="form-check-label" for="isTeacher">
                                        Register as Teacher
                                    </label>
                                </div>

                                <div class="form-check mb-3">
                                    <input 
                                        v-model="isStudent"
                                        type="checkbox" 
                                        class="form-check-input" 
                                        id="isStudent"
                                    />
                                    <label class="form-check-label" for="isStudent">
                                        Register as Student
                                    </label>
                                </div>

                                <button 
                                    type="submit" 
                                    class="btn btn-primary w-100"
                                    :disabled="loading"
                                >
                                    {{ loading ? 'Creating Account...' : 'Register' }}
                                </button>
                            </form>
                            <p class="mt-3 text-center">
                                Already registered?
                                <router-link to="/login">Sign in</router-link>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>