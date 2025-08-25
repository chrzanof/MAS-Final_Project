<script>
import axios from 'axios'

export default {
  data() {
    return {
      courses: [],
      selectedCourse: 0,
      searchQuery: '',
      filteredCourses: []
    }
  },
  mounted() {
    this.fetchCourses();
  },
  methods: {
    async fetchCourses() {
      try {
        const response = await axios.get("http://localhost:8080/api/courses")
        this.courses = response.data
        this.filteredCourses = this.courses
        console.log(this.courses)
      } catch (error) {
        console.error('Error fetching courses:', error)
      }
    },
    async searchCourses() {
      
      if (this.searchQuery === '') {
        this.filteredCourses = this.courses
        return
      }
      this.filteredCourses = this.courses.filter(course => course.title.includes(this.searchQuery) || course.description.includes(this.searchQuery))
    },
    selectCourse(courseIndex) {
      this.selectedCourse = courseIndex;
    }
  }
}
</script>

<template>
  <div class="row">
    <!-- add a search bar -->
    <div class="col-md-12">
      <input type="text" class="form-control" placeholder="Search courses" v-model="searchQuery" @input="searchCourses">
    </div>
    <div class="col-md-12">
      <table class="table table-hover" id="courses-table">
        <thead>
          <tr>
            <th>Course Name</th>
            <th>Title</th>
            <th>Description</th>
          </tr>
        </thead>
        <tbody>
          <template v-if="filteredCourses">
            <tr v-for="(course, index) in filteredCourses" :key="index" @click="selectCourse(index)" :class="{ 'table-active': selectedCourse == index }">
              <td>{{ course.id }}</td>
              <td>{{ course.title }}</td>
              <td>{{ course.description }}</td>
            </tr>
          </template>
        </tbody>
      </table>
    </div>
  </div>
</template>

<style scoped>

</style>