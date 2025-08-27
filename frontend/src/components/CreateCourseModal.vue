<script>
import axios from 'axios'

export default {
  emits: ['close', 'courseCreated'],
  data() {
    return {
      form: {
        title: '',
        description: '',
        availableFrom: '',
        availableTo: ''
      },
      loading: false,
      error: ''
    }
  },
  methods: {
    async createCourse() {
      // Validate form
      if (!this.form.title.trim()) {
        this.error = 'Course title is required'
        return
      }
      if (!this.form.description.trim()) {
        this.error = 'Course description is required'
        return
      }
      if (!this.form.availableFrom) {
        this.error = 'Available from date is required'
        return
      }
      if (!this.form.availableTo) {
        this.error = 'Available to date is required'
        return
      }
      
      // Validate that Available To is after Available From
      if (this.form.availableTo <= this.form.availableFrom) {
        this.error = 'Available To date must be after Available From date'
        return
      }

      this.loading = true
      this.error = ''

      try {
        const courseData = {
          title: this.form.title.trim(),
          description: this.form.description.trim(),
          availableFrom: this.form.availableFrom + 'T00:00:00', // Convert to datetime format
          availableTo: this.form.availableTo + 'T23:59:59' // Convert to datetime format
        }

        // const response = await axios.post('http://localhost:8080/api/courses', courseData)
        
        // For now, simulate a successful response
        const mockResponse = {
          id: Date.now(), // Use timestamp as mock ID
          ...courseData
        }
        
        // Emit success event with the mock created course
        this.$emit('courseCreated', mockResponse)
        this.resetForm()
        this.$emit('close')
        
      } catch (error) {
        console.error('Error creating course:', error)
        this.error = error.response?.data || 'Failed to create course. Please try again.'
      } finally {
        this.loading = false
      }
    },
    
    resetForm() {
      this.form = {
        title: '',
        description: '',
        availableFrom: '',
        availableTo: ''
      }
      this.error = ''
    },
    
    closeModal() {
      this.resetForm()
      this.$emit('close')
    }
  }
}
</script>

<template>
    <div class="modal fade show" style="display: block;" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="false">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5" id="staticBackdropLabel">Create New Course</h1>
        <button type="button" class="btn-close" @click="closeModal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <!-- Error Alert -->
        <div v-if="error" class="alert alert-danger" role="alert">
          {{ error }}
        </div>
        
        <form @submit.prevent="createCourse">
          <div class="mb-3">
            <label for="courseTitle" class="form-label">Course Title <span class="text-danger">*</span></label>
            <input 
              type="text" 
              class="form-control" 
              id="courseTitle" 
              v-model="form.title"
              placeholder="Enter course title"
              :disabled="loading"
              required
            >
          </div>
          
          <div class="mb-3">
            <label for="courseDescription" class="form-label">Course Description <span class="text-danger">*</span></label>
            <textarea 
              class="form-control" 
              id="courseDescription" 
              rows="3" 
              v-model="form.description"
              placeholder="Enter course description"
              :disabled="loading"
              required
            ></textarea>
          </div>
          
          <div class="mb-3">
            <label for="availableFrom" class="form-label">Available From <span class="text-danger">*</span></label>
            <input 
              type="date" 
              class="form-control" 
              id="availableFrom" 
              v-model="form.availableFrom"
              :disabled="loading"
              :min="new Date().toISOString().split('T')[0]"
              required
            >
            <div class="form-text">Select when the course becomes available to students</div>
          </div>
          
                    <div class="mb-3">
            <label for="availableTo" class="form-label">Available To <span class="text-danger">*</span></label>
            <input 
              type="date" 
              class="form-control" 
              id="availableTo" 
              v-model="form.availableTo"
              :disabled="loading"
              :min="form.availableFrom || new Date().toISOString().split('T')[0]"
              required
            >
             <div class="form-text">Select when the course expires</div>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button 
          type="button" 
          class="btn btn-secondary" 
          @click="closeModal"
          :disabled="loading"
        >
          Cancel
        </button>
        <button 
          type="button" 
          class="btn btn-primary" 
          @click="createCourse"
          :disabled="loading"
        >
          <span v-if="loading" class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
          {{ loading ? 'Creating...' : 'Confirm' }}
        </button>
      </div>
    </div>
  </div>
</div>
</template>

<style scoped>
.modal.show {
  background-color: rgba(0, 0, 0, 0.5);
}
</style>