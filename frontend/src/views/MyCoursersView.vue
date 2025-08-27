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
  }
}
</script>

<template>
  <div>
    
    <div class="row">
    <div class="col-md-6">
      <table class="table table-hover" id="courses-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Description</th>
            <th>
              <button type="button" class="btn btn-primary" @click="toggleCreateModal('course')">
                +
              </button>
            </th>
          </tr>
        </thead>
        <tbody>
          <template v-if="courses">
            <tr v-for="(course, index) in courses" :key="index" @click="selectCourse(index)" :class="{ 'table-active': selectedCourse == index }">
              <td>{{ course.id }}</td>
              <td>{{ course.title }}</td>
              <td>{{ course.description }}</td>
            </tr>
          </template>
        </tbody>
      </table>
    </div>
    
    <div class="col-md-6">
      <div class="row mb-4">
        <div class="col-12">
          <table class="table" id="lessons-table">
            <thead>
              <tr>
                <th>Lesson Name</th>
                <th>Description</th>
                <th>
              <button type="button" class="btn btn-primary" @click="toggleCreateModal('lesson')">
                +
              </button>
            </th>
              </tr>
            </thead>
            <tbody>
              <template v-if="courses && courses[selectedCourse]">
                <tr v-for="lesson in courses[selectedCourse].lessons" :key="lesson.id">
                  <td>{{ lesson.title }}</td>
                  <td v-if="lesson.description">{{ lesson.description }}</td>
                  <td v-else>No description</td>
                </tr>
              </template>
            </tbody>
          </table>
        </div>
      </div>
      
      <div class="row">
        <div class="col-12">
          <table class="table" id="quizzes-table">
            <thead>
              <tr>
                <th>Quiz Name</th>
                <th>Description</th>
                <th>
              <button type="button" class="btn btn-primary" @click="toggleCreateModal('quiz')">
                +
              </button>
            </th>
              </tr>
            </thead>
            <tbody>
              <template v-if="courses && courses[selectedCourse]">
                <tr v-for="quiz in courses[selectedCourse].quizzes" :key="quiz.id">
                  <td>{{ quiz.title }}</td>
                  <td>{{ quiz.description }}</td>
                </tr>
              </template>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    </div>

    <CreateCourseModal v-if="showCreateModal && createModalType === 'course'" @close="showCreateModal = false"/>
    <CreateLessonModal v-if="showCreateModal && createModalType === 'lesson'" @close="showCreateModal = false"/>
    <CreateQuizModal v-if="showCreateModal && createModalType === 'quiz'" @close="showCreateModal = false"/>
  </div>
</template>

<style scoped>

</style>