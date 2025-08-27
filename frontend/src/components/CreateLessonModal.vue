<script>
export default {
  props: {
    courseId: {
      type: Number,
      required: true
    }
  },
  emits: ['close', 'lessonCreated'],
  data() {
    return {
      form: {
        title: '',
        description: '',
        content: ''
      },
      loading: false,
      error: ''
    }
  },
  methods: {
    async createLesson() {
      // Validate form
      if (!this.form.title.trim()) {
        this.error = 'Lesson title is required'
        return
      }
      if (!this.form.description.trim()) {
        this.error = 'Lesson description is required'
        return
      }
      if (!this.form.content.trim()) {
        this.error = 'Lesson content is required'
        return
      }

      this.loading = true
      this.error = ''

      try {
        const lessonData = {
          title: this.form.title.trim(),
          description: this.form.description.trim(),
          content: this.form.content.trim(),
          courseId: this.courseId,
          lessonNumber: Date.now() % 1000 // Mock lesson number
        }

        // For now, simulate a successful response
        const mockResponse = {
          id: Date.now(),
          ...lessonData
        }
        
        // Emit success event with the mock created lesson
        this.$emit('lessonCreated', mockResponse)
        this.resetForm()
        this.$emit('close')
        
      } catch (error) {
        console.error('Error creating lesson:', error)
        this.error = 'Failed to create lesson. Please try again.'
      } finally {
        this.loading = false
      }
    },
    
    resetForm() {
      this.form = {
        title: '',
        description: '',
        content: ''
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
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5" id="staticBackdropLabel">Create New Lesson</h1>
        <button type="button" class="btn-close" @click="closeModal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <!-- Error Alert -->
        <div v-if="error" class="alert alert-danger" role="alert">
          {{ error }}
        </div>
        
        <form @submit.prevent="createLesson">
          <div class="mb-3">
            <label for="lessonTitle" class="form-label">Lesson Title <span class="text-danger">*</span></label>
            <input 
              type="text" 
              class="form-control" 
              id="lessonTitle" 
              v-model="form.title"
              placeholder="Enter lesson title"
              :disabled="loading"
              required
            >
          </div>
          
          <div class="mb-3">
            <label for="lessonDescription" class="form-label">Lesson Description <span class="text-danger">*</span></label>
            <textarea 
              class="form-control" 
              id="lessonDescription" 
              rows="3" 
              v-model="form.description"
              placeholder="Enter lesson description"
              :disabled="loading"
              required
            ></textarea>
          </div>
          
          <div class="mb-3">
            <label for="lessonContent" class="form-label">Lesson Content <span class="text-danger">*</span></label>
            <textarea 
              class="form-control" 
              id="lessonContent" 
              rows="8" 
              v-model="form.content"
              placeholder="Enter the main content of the lesson..."
              :disabled="loading"
              required
            ></textarea>
            <div class="form-text">Provide detailed lesson content, instructions, and materials</div>
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
          @click="createLesson"
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