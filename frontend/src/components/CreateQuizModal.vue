<script>
export default {
  props: {
    courseId: {
      type: Number,
      required: true
    }
  },
  emits: ['close', 'quizCreated'],
  data() {
    return {
      form: {
        title: '',
        numberOfQuestions: 1
      },
      questions: [],
      loading: false,
      error: '',
      showSuccessConfirmation: false,
      createdQuiz: null
    }
  },
  watch: {
    'form.numberOfQuestions'(newVal, oldVal) {
      this.adjustQuestions(newVal)
    }
  },
  mounted() {
    this.initializeQuestions()
  },
  methods: {
    initializeQuestions() {
      this.questions = [{
        type: 'multichoice',
        content: '',
        options: ['', ''],
        correctOption: 0,
        answer: ''
      }]
    },
    
    adjustQuestions(numberOfQuestions) {
      const currentLength = this.questions.length
      
      if (numberOfQuestions > currentLength) {
        // Add new questions
        for (let i = currentLength; i < numberOfQuestions; i++) {
          this.questions.push({
            type: 'multichoice',
            content: '',
            options: ['', ''],
            correctOption: 0,
            answer: ''
          })
        }
      } else if (numberOfQuestions < currentLength) {
        // Remove questions
        this.questions = this.questions.slice(0, numberOfQuestions)
      }
    },
    
    addOption(questionIndex) {
      this.questions[questionIndex].options.push('')
    },
    
    removeOption(questionIndex, optionIndex) {
      if (this.questions[questionIndex].options.length > 2) {
        this.questions[questionIndex].options.splice(optionIndex, 1)
        // Adjust correct option if needed
        if (this.questions[questionIndex].correctOption >= optionIndex) {
          this.questions[questionIndex].correctOption = Math.max(0, this.questions[questionIndex].correctOption - 1)
        }
      }
    },
    
    validateForm() {
      if (!this.form.title.trim()) {
        return 'Quiz title is required'
      }
      
      if (this.form.numberOfQuestions < 1 || this.form.numberOfQuestions > 20) {
        return 'Number of questions must be between 1 and 20'
      }
      
      for (let i = 0; i < this.questions.length; i++) {
        const question = this.questions[i]
        
        if (!question.content.trim()) {
          return `Question ${i + 1} content is required`
        }
        
        if (question.type === 'multichoice') {
          const validOptions = question.options.filter(opt => opt.trim())
          if (validOptions.length < 2) {
            return `Question ${i + 1} must have at least 2 options`
          }
          if (!question.options[question.correctOption] || !question.options[question.correctOption].trim()) {
            return `Question ${i + 1} must have a valid correct option selected`
          }
        } else if (question.type === 'open') {
          if (!question.answer.trim()) {
            return `Question ${i + 1} answer is required`
          }
        }
      }
      
      return null
    },
    
    async createQuiz() {
      const validationError = this.validateForm()
      if (validationError) {
        this.error = validationError
        return
      }
      
      this.loading = true
      this.error = ''
      
      try {
        const quizData = {
          title: this.form.title.trim(),
          description: '', // Can add description field if needed
          courseId: this.courseId,
          positionIndex: Date.now() % 1000,
          questions: this.questions.map((q, index) => ({
            text: q.content.trim(),
            points: 1, // Default points
            positionIndex: index,
            type: q.type,
            ...(q.type === 'multichoice' ? {
              options: q.options.filter(opt => opt.trim()),
              correctOptionIndex: q.correctOption
            } : {
              correctAnswer: q.answer.trim()
            })
          }))
        }
        
        // For now, simulate a successful response
        const mockResponse = {
          id: Date.now(),
          ...quizData
        }
        
        // Store created quiz and show success confirmation
        this.createdQuiz = mockResponse
        this.showSuccessConfirmation = true
        
        // Emit success event with the mock created quiz
        this.$emit('quizCreated', mockResponse)
        
      } catch (error) {
        console.error('Error creating quiz:', error)
        this.error = 'Failed to create quiz. Please try again.'
      } finally {
        this.loading = false
      }
    },
    
    resetForm() {
      this.form = {
        title: '',
        numberOfQuestions: 1
      }
      this.initializeQuestions()
      this.error = ''
      this.showSuccessConfirmation = false
      this.createdQuiz = null
    },
    
    closeModal() {
      this.resetForm()
      this.$emit('close')
    },
    
    onSuccessOk() {
      // Close the modal after success acknowledgment
      this.resetForm()
      this.$emit('close')
    }
  }
}
</script>

<template>
  <div class="modal fade show" style="display: block;" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="false">
    <div class="modal-dialog modal-xl">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="staticBackdropLabel">
            {{ showSuccessConfirmation ? 'Quiz Created Successfully' : 'Create New Quiz' }}
          </h1>
          <button type="button" class="btn-close" @click="closeModal" aria-label="Close"></button>
        </div>
        
        <div class="modal-body" style="max-height: 70vh; overflow-y: auto;">
          <!-- Success Confirmation -->
          <div v-if="showSuccessConfirmation">
            <div class="alert alert-success text-center" role="alert">
              <h4 class="alert-heading mb-1">Success!</h4>
              <p class="mb-0">Quiz has been saved!</p>
            </div>
            
            <div class="text-center">
              <button 
                type="button" 
                class="btn btn-primary px-4"
                @click="onSuccessOk"
              >
                OK
              </button>
            </div>
          </div>
          
          <!-- Quiz Creation Form -->
          <div v-else>
            <!-- Error Alert -->
            <div v-if="error" class="alert alert-danger" role="alert">
              {{ error }}
            </div>
            
            <form @submit.prevent="createQuiz">
            <!-- Basic Quiz Info -->
            <div class="row mb-4">
              <div class="col-md-8">
                <label for="quizTitle" class="form-label">Quiz Title <span class="text-danger">*</span></label>
                <input 
                  type="text" 
                  class="form-control" 
                  id="quizTitle" 
                  v-model="form.title"
                  placeholder="Enter quiz title"
                  :disabled="loading"
                  required
                >
              </div>
              <div class="col-md-4">
                <label for="numberOfQuestions" class="form-label">Number of Questions <span class="text-danger">*</span></label>
                <input 
                  type="number" 
                  class="form-control" 
                  id="numberOfQuestions" 
                  v-model.number="form.numberOfQuestions"
                  min="1" 
                  max="20"
                  :disabled="loading"
                  required
                >
              </div>
            </div>
            
            <hr>
            
            <!-- Questions Section -->
            <div class="questions-section">
              <h5 class="mb-3">Questions</h5>
              
              <div 
                v-for="(question, questionIndex) in questions" 
                :key="questionIndex"
                class="question-card card mb-4"
              >
                <div class="card-header">
                  <div class="d-flex justify-content-between align-items-center">
                    <h6 class="mb-0">Question {{ questionIndex + 1 }}</h6>
                    <select 
                      class="form-select form-select-sm" 
                      style="width: auto;"
                      v-model="question.type"
                      :disabled="loading"
                    >
                      <option value="multichoice">Multiple Choice</option>
                      <option value="open">Open Question</option>
                    </select>
                  </div>
                </div>
                
                <div class="card-body">
                  <!-- Question Content -->
                  <div class="mb-3">
                    <label class="form-label">Question Content <span class="text-danger">*</span></label>
                    <textarea 
                      class="form-control" 
                      v-model="question.content"
                      placeholder="Enter the question..."
                      rows="2"
                      :disabled="loading"
                      required
                    ></textarea>
                  </div>
                  
                  <!-- Multiple Choice Options -->
                  <div v-if="question.type === 'multichoice'">
                    <label class="form-label">Answer Options <span class="text-danger">*</span></label>
                    <div 
                      v-for="(option, optionIndex) in question.options" 
                      :key="optionIndex"
                      class="input-group mb-2"
                    >
                      <div class="input-group-text">
                        <input 
                          type="radio" 
                          :name="`question-${questionIndex}-correct`"
                          :value="optionIndex"
                          v-model="question.correctOption"
                          :disabled="loading"
                          title="Mark as correct answer"
                        >
                      </div>
                      <input 
                        type="text" 
                        class="form-control" 
                        v-model="question.options[optionIndex]"
                        :placeholder="`Option ${optionIndex + 1}`"
                        :disabled="loading"
                        required
                      >
                      <button 
                        type="button" 
                        class="btn btn-outline-danger"
                        @click="removeOption(questionIndex, optionIndex)"
                        :disabled="loading || question.options.length <= 2"
                        title="Remove option"
                      >
                        ×
                      </button>
                    </div>
                    
                    <button 
                      type="button" 
                      class="btn btn-outline-primary btn-sm"
                      @click="addOption(questionIndex)"
                      :disabled="loading || question.options.length >= 6"
                    >
                      + Add Option
                    </button>
                    
                    <div class="form-text">
                      Select the radio button next to the correct answer
                    </div>
                  </div>
                  
                  <!-- Open Question Answer -->
                  <div v-if="question.type === 'open'">
                    <label class="form-label">Correct Answer <span class="text-danger">*</span></label>
                    <textarea 
                      class="form-control" 
                      v-model="question.answer"
                      placeholder="Enter the correct answer..."
                      rows="2"
                      :disabled="loading"
                      required
                    ></textarea>
                  </div>
                </div>
              </div>
            </div>
            </form>
          </div>
        </div>
        
        <!-- Modal Footer - only show during form creation -->
        <div class="modal-footer" v-if="!showSuccessConfirmation">
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
            @click="createQuiz"
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

.question-card {
  border: 1px solid #dee2e6;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.question-card .card-header {
  background-color: #f8f9fa;
  border-bottom: 1px solid #dee2e6;
}

.questions-section {
  max-height: 50vh;
}

.input-group .btn {
  border-left: none;
}

.input-group .input-group-text {
  background-color: #e9ecef;
}

.input-group input[type="radio"] {
  margin: 0;
}

.form-text {
  color: #6c757d;
  font-size: 0.875em;
}
</style>