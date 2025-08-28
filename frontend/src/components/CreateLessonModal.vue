<script>
import axios from 'axios'
import CreateQuizModal from './CreateQuizModal.vue'

export default {
  components: {
    CreateQuizModal
  },
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
      error: '',
      showSuccessConfirmation: false,
      showQuizConfirmation: false,
      createdLesson: null,
      showQuizModal: false
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
          lessonNumber: Date.now() % 1000 // Mock lesson number for now
        }

        const response = await axios.post(`http://localhost:8080/api/courses/${this.courseId}/lessons`, lessonData)
        
       
        this.createdLesson = response.data
        this.showSuccessConfirmation = true
        
        
        this.$emit('lessonCreated', response.data)
        
      } catch (error) {
        console.error('Error creating lesson:', error)
        this.error = error.response?.data || 'Failed to create lesson. Please try again.'
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
      this.showSuccessConfirmation = false
      this.showQuizConfirmation = false
      this.createdLesson = null
      this.showQuizModal = false
    },
    
    closeModal() {
      this.resetForm()
      this.$emit('close')
    },
    
    onCreateAnotherLessonYes() {
      
      this.form = {
        title: '',
        description: '',
        content: ''
      }
      this.showSuccessConfirmation = false
      this.createdLesson = null
      this.error = ''
    },
    
    onCreateAnotherLessonNo() {
      
      this.showSuccessConfirmation = false
      this.showQuizConfirmation = true
    },
    
    onCreateQuizYes() {
      
      this.showQuizConfirmation = false
      this.showQuizModal = true
    },
    
    onCreateQuizNo() {
      
      this.resetForm()
      this.$emit('close')
    },
    
    onQuizCreated(quiz) {
      console.log('Quiz created:', quiz)
    },
    
    onQuizModalClose() {
      this.resetForm()
      this.$emit('close')
    }
  }
}
</script>

<template>
  <div>
    <div class="modal fade show" style="display: block;" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="false">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h1 class="modal-title fs-5" id="staticBackdropLabel">
              {{ showSuccessConfirmation ? 'Lesson Created Successfully' : 
                 showQuizConfirmation ? 'Create Quiz?' : 'Create New Lesson' }}
            </h1>
            <button type="button" class="btn-close" @click="closeModal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            
            <div v-if="showSuccessConfirmation">
              <div class="alert alert-success text-center" role="alert">
                <h4 class="alert-heading mb-1">Success!</h4>
                <p class="mb-0">Lesson has been saved!</p>
              </div>
              
              <div class="text-center">
                <h5 class="mb-3">Would you like to create another lesson?</h5>
                <div class="d-flex justify-content-center gap-3">
                  <button 
                    type="button" 
                    class="btn btn-secondary px-4"
                    @click="onCreateAnotherLessonNo"
                  >
                    No
                  </button>
                  <button 
                    type="button" 
                    class="btn btn-primary px-4"
                    @click="onCreateAnotherLessonYes"
                  >
                    Yes
                  </button>
                </div>
              </div>
            </div>
            
            
            <div v-else-if="showQuizConfirmation">
              <div class="text-center">
                <h5 class="mb-3">Do you want to create quiz?</h5>
                <div class="d-flex justify-content-center gap-3">
                  <button 
                    type="button" 
                    class="btn btn-secondary px-4"
                    @click="onCreateQuizNo"
                  >
                    No
                  </button>
                  <button 
                    type="button" 
                    class="btn btn-primary px-4"
                    @click="onCreateQuizYes"
                  >
                    Yes
                  </button>
                </div>
              </div>
            </div>
            
            
            <div v-else>
              
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
          </div>
          
          
          <div class="modal-footer" v-if="!showSuccessConfirmation && !showQuizConfirmation">
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

    <CreateQuizModal 
      v-if="showQuizModal"
      :courseId="courseId"
      @close="onQuizModalClose"
      @quizCreated="onQuizCreated"
    />
  </div>
</template>

<style scoped>
.modal.show {
  background-color: rgba(0, 0, 0, 0.5);
}
</style>