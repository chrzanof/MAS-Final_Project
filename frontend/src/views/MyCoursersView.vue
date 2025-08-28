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
                  </tr>
                </thead>
                <tbody>
                  <template v-if="courses && courses[selectedCourse] && courses[selectedCourse].lessons && Object.keys(courses[selectedCourse].lessons).length > 0">
                    <tr v-for="lesson in sortedLessons" :key="lesson.id">
                      <td class="fw-semibold">
                        <span class="badge bg-secondary me-2">{{ lesson.lessonNumber }}</span>
                        {{ lesson.title }}
                      </td>
                      <td class="text-muted">
                        <span v-if="lesson.description">{{ lesson.description }}</span>
                        <em v-else class="text-muted">No description</em>
                      </td>
                    </tr>
                  </template>
                  <tr v-else>
                    <td colspan="2" class="text-center text-muted py-4">
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
                  </tr>
                </thead>
                <tbody>
                  <template v-if="courses && courses[selectedCourse] && courses[selectedCourse].quizzes && Object.keys(courses[selectedCourse].quizzes).length > 0">
                    <tr v-for="quiz in Object.values(courses[selectedCourse].quizzes)" :key="quiz.id">
                      <td class="fw-semibold">{{ quiz.title }}</td>
                      <td class="text-muted">{{ quiz.description }}</td>
                    </tr>
                  </template>
                  <tr v-else>
                    <td colspan="2" class="text-center text-muted py-4">
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

</style>