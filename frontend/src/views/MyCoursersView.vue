<script>
import axios from 'axios'

export default {
  data() {
    return {
      courses: [],
      selectedCourse: 0
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
    }
  }
}
</script>

<template>
  <div class="row">
    <div class="col-md-6">
      <table class="table table-hover" id="courses-table">
        <thead>
          <tr>
            <th>Course Name</th>
            <th>Title</th>
            <th>Description</th>
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
                <th>Content</th>
              </tr>
            </thead>
            <tbody>
              <template v-if="courses && courses[selectedCourse]">
                <tr v-for="lesson in courses[selectedCourse].lessons" :key="lesson.id">
                  <td>{{ lesson.title }}</td>
                  <td>{{ lesson.content }}</td>
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
</template>

<style scoped>

</style>