<script>
import axios from 'axios'
import CreateCourseModal from '@/components/CreateCourseModal.vue'; 
import CreateLessonModal from '@/components/CreateLessonModal.vue';
import CreateQuizModal from '@/components/CreateQuizModal.vue';

export default {
  components: {
    CreateCourseModal,
    CreateLessonModal,
    CreateQuizModal
  },
  data() {
    return {
      courses: [],
      selectedCourse: 0,
      showCreateModal: false,
      createModalType: null
    }
  },
  mounted() {
    this.fetchCourses();
  },
  computed: {
    sortedLessons() {
      if (!this.courses || !this.courses[this.selectedCourse] || !this.courses[this.selectedCourse].lessons) {
        return []
      }
      return Object.values(this.courses[this.selectedCourse].lessons)
        .sort((a, b) => a.lessonNumber - b.lessonNumber)
    },
    sortedQuizzes() {
      if (!this.courses || !this.courses[this.selectedCourse] || !this.courses[this.selectedCourse].quizzes) {
        return []
      }
      return this.courses[this.selectedCourse].quizzes
        .sort((a, b) => a.positionIndex - b.positionIndex)
    }
  },
  methods: {
    async fetchCourses() {
      try {
        const response = await axios.get("http://localhost:8080/api/courses/my-courses")
        this.courses = response.data
        console.log(this.courses)
      } catch (error) {
        console.error('Error fetching courses:', error)
      }
    },
    selectCourse(courseIndex) {
      this.selectedCourse = courseIndex;
    },
    toggleCreateModal(type) {
      this.showCreateModal = !this.showCreateModal;
      this.createModalType = type;
    },
    onLessonCreated() {
      this.showCreateModal = false;
      this.fetchCourses(); 
    },
    async moveLessonUp(lesson) {
      if (lesson.lessonNumber <= 1) return;
      
      try {
        await axios.put(`http://localhost:8080/api/courses/${this.courses[this.selectedCourse].id}/lessons/${lesson.id}/move-up`);
        await this.fetchCourses();
      } catch (error) {
        console.error('Error moving lesson up:', error);
        alert('Failed to move lesson up. Please try again.');
      }
    },
    async moveLessonDown(lesson) {
      if (lesson.lessonNumber >= this.sortedLessons.length) return;
      
      try {
        await axios.put(`http://localhost:8080/api/courses/${this.courses[this.selectedCourse].id}/lessons/${lesson.id}/move-down`);
        await this.fetchCourses();
      } catch (error) {
        console.error('Error moving lesson down:', error);
        alert('Failed to move lesson down. Please try again.');
      }
    },
    async moveQuizUp(quiz) {
      if (quiz.positionIndex <= 0) return;
      
      try {
        await axios.put(`http://localhost:8080/api/courses/${this.courses[this.selectedCourse].id}/quizzes/${quiz.id}/move-up`);
        await this.fetchCourses();
      } catch (error) {
        console.error('Error moving quiz up:', error);
        alert('Failed to move quiz up. Please try again.');
      }
    },
    async moveQuizDown(quiz) {
      if (quiz.positionIndex >= this.sortedQuizzes.length - 1) return;
      
      try {
        await axios.put(`http://localhost:8080/api/courses/${this.courses[this.selectedCourse].id}/quizzes/${quiz.id}/move-down`);
        await this.fetchCourses();
      } catch (error) {
        console.error('Error moving quiz down:', error);
        alert('Failed to move quiz down. Please try again.');
      }
    },
  }
}
</script>

<template>
  <div class="container-fluid py-4">
    <div class="row g-4">
      <!-- Courses Section -->
      <div class="col-lg-6">
        <div class="card shadow-sm">
          <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
            <h5 class="mb-0">My Courses</h5>
            <button type="button" class="btn btn-light btn-sm" @click="toggleCreateModal('course')">
              Add Course
            </button>
          </div>
          <div class="card-body p-0">
            <div class="table-responsive">
              <table class="table table-striped table-hover mb-0" id="courses-table">
                <thead class="table-light">
                  <tr>
                    <th scope="col" class="text-center" style="width: 80px;">#</th>
                    <th scope="col">Title</th>
                    <th scope="col">Description</th>
                  </tr>
                </thead>
                <tbody>
                  <template v-if="courses && courses.length > 0">
                    <tr v-for="(course, index) in courses" 
                        :key="index" 
                        @click="selectCourse(index)" 
                        :class="{ 'table-active': selectedCourse == index }"
                        style="cursor: pointer;">
                      <td class="text-center fw-bold text-muted">{{ course.id }}</td>
                      <td class="fw-semibold">{{ course.title }}</td>
                      <td class="text-muted">{{ course.description }}</td>
                    </tr>
                  </template>
                  <tr v-else>
                    <td colspan="3" class="text-center text-muted py-4">
                      <em>No courses available</em>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Lessons and Quizzes Section -->
      <div class="col-lg-6">
        <!-- Lessons -->
        <div class="card shadow-sm mb-4">
          <div class="card-header bg-success text-white d-flex justify-content-between align-items-center">
            <h5 class="mb-0">Lessons</h5>
            <button type="button" 
                    class="btn btn-light btn-sm" 
                    @click="toggleCreateModal('lesson')"
                    :disabled="!courses || courses.length === 0">
              Add Lesson
            </button>
          </div>
          <div class="card-body p-0">
            <div class="table-responsive">
              <table class="table table-striped mb-0" id="lessons-table">
                <thead class="table-light">
                  <tr>
                    <th scope="col">Lesson Name</th>
                    <th scope="col">Description</th>
                    <th scope="col" width="120">Actions</th>
                  </tr>
                </thead>
                <tbody>
                  <template v-if="courses && courses[selectedCourse] && courses[selectedCourse].lessons && Object.keys(courses[selectedCourse].lessons).length > 0">
                    <tr v-for="(lesson, index) in sortedLessons" :key="lesson.id">
                      <td class="fw-semibold">
                        <span class="badge bg-secondary me-2">{{ lesson.lessonNumber }}</span>
                        {{ lesson.title }}
                      </td>
                      <td class="text-muted">
                        <span v-if="lesson.description">{{ lesson.description }}</span>
                        <em v-else class="text-muted">No description</em>
                      </td>
                      <td>
                        <div class="btn-group" role="group">
                          <button 
                            type="button" 
                            :class="lesson.lessonNumber === 1 ? 'btn btn-light btn-sm disabled-arrow' : 'btn btn-outline-primary btn-sm active-arrow'"
                            @click="moveLessonUp(lesson)"
                            :disabled="lesson.lessonNumber === 1"
                            :title="lesson.lessonNumber === 1 ? 'Already at top' : 'Move Up'"
                          >
                            ↑
                          </button>
                          <button 
                            type="button" 
                            :class="lesson.lessonNumber === sortedLessons.length ? 'btn btn-light btn-sm disabled-arrow' : 'btn btn-outline-primary btn-sm active-arrow'"
                            @click="moveLessonDown(lesson)"
                            :disabled="lesson.lessonNumber === sortedLessons.length"
                            :title="lesson.lessonNumber === sortedLessons.length ? 'Already at bottom' : 'Move Down'"
                          >
                            ↓
                          </button>
                        </div>
                      </td>
                    </tr>
                  </template>
                  <tr v-else>
                    <td colspan="3" class="text-center text-muted py-4">
                      <em>{{ courses && courses.length > 0 ? 'No lessons available' : 'Select a course to view lessons' }}</em>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
        
        <!-- Quizzes -->
        <div class="card shadow-sm">
          <div class="card-header bg-info text-white d-flex justify-content-between align-items-center">
            <h5 class="mb-0">Quizzes</h5>
            <button type="button" 
                    class="btn btn-light btn-sm" 
                    @click="toggleCreateModal('quiz')"
                    :disabled="!courses || courses.length === 0">
              Add Quiz
            </button>
          </div>
          <div class="card-body p-0">
            <div class="table-responsive">
              <table class="table table-striped mb-0" id="quizzes-table">
                <thead class="table-light">
                  <tr>
                    <th scope="col">Quiz Name</th>
                    <th scope="col">Description</th>
                    <th scope="col" width="120">Actions</th>
                  </tr>
                </thead>
                <tbody>
                  <template v-if="courses && courses[selectedCourse] && courses[selectedCourse].quizzes && Object.keys(courses[selectedCourse].quizzes).length > 0">
                    <tr v-for="(quiz, index) in sortedQuizzes" :key="quiz.id">
                      <td class="fw-semibold">
                        <span class="badge bg-secondary me-2">{{ quiz.positionIndex }}</span>
                        {{ quiz.title }}
                      </td>
                      <td class="text-muted">{{ quiz.description }}</td>
                      <td>
                        <div class="btn-group" role="group">
                          <button 
                            type="button" 
                            :class="quiz.positionIndex === 0 ? 'btn btn-light btn-sm disabled-arrow' : 'btn btn-outline-primary btn-sm active-arrow'"
                            @click="moveQuizUp(quiz)"
                            :disabled="quiz.positionIndex === 0"
                            :title="quiz.positionIndex === 0 ? 'Already at top' : 'Move Up'"
                          >
                            ↑
                          </button>
                          <button 
                            type="button" 
                            :class="quiz.positionIndex === sortedQuizzes.length - 1 ? 'btn btn-light btn-sm disabled-arrow' : 'btn btn-outline-primary btn-sm active-arrow'"
                            @click="moveQuizDown(quiz)"
                            :disabled="quiz.positionIndex === sortedQuizzes.length - 1"
                            :title="quiz.positionIndex === sortedQuizzes.length - 1 ? 'Already at bottom' : 'Move Down'"
                          >
                            ↓
                          </button>
                        </div>
                      </td>
                    </tr>
                  </template>
                  <tr v-else>
                    <td colspan="3" class="text-center text-muted py-4">
                      <em>{{ courses && courses.length > 0 ? 'No quizzes available' : 'Select a course to view quizzes' }}</em>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <CreateCourseModal v-if="showCreateModal && createModalType === 'course'" @close="showCreateModal = false"/>
    <CreateLessonModal :courseId="courses[selectedCourse].id" v-if="showCreateModal && createModalType === 'lesson'" @close="showCreateModal = false" @lessonCreated="onLessonCreated"/>
    <CreateQuizModal :courseId="courses[selectedCourse].id" v-if="showCreateModal && createModalType === 'quiz'" @close="showCreateModal = false"/>
  </div>
</template>

<style scoped>
.disabled-arrow {
  opacity: 0.3;
  color: #6c757d !important;
  border-color: #dee2e6 !important;
  background-color: #f8f9fa !important;
  cursor: not-allowed !important;
}

.disabled-arrow:hover {
  opacity: 0.3 !important;
  color: #6c757d !important;
  border-color: #dee2e6 !important;
  background-color: #f8f9fa !important;
  transform: none !important;
}

.active-arrow {
  cursor: pointer;
  transition: all 0.2s ease;
}

.active-arrow:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}
</style>