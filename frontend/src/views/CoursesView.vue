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
  <div class="container-fluid py-4">
    <!-- Search Section -->
    <div class="row mb-4">
      <div class="col-lg-6 mx-auto">
        <div class="card shadow-sm">
          <div class="card-body">
            <div class="input-group input-group-lg">
              <input 
                type="text" 
                class="form-control form-control-lg" 
                placeholder="Search courses by title or description..." 
                v-model="searchQuery" 
                @input="searchCourses"
              >
              <button 
                class="btn btn-outline-secondary" 
                type="button" 
                @click="searchQuery = ''; searchCourses()"
                v-if="searchQuery"
              >
                Clear
              </button>
            </div>
            <small class="text-muted mt-2 d-block" v-if="searchQuery">
              Found {{ filteredCourses.length }} course(s) matching "{{ searchQuery }}"
            </small>
          </div>
        </div>
      </div>
    </div>

    <!-- Courses Table Section -->
    <div class="row">
      <div class="col-12">
        <div class="card shadow-sm">
          <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
            <h5 class="mb-0">
              Available Courses 
              <span class="badge bg-light text-primary ms-2">{{ filteredCourses.length }}</span>
            </h5>
          </div>
          <div class="card-body p-0">
            <div class="table-responsive">
              <table class="table table-striped table-hover mb-0" id="courses-table">
                <thead class="table-light">
                  <tr>
                    <th scope="col" class="text-center" style="width: 80px;">#</th>
                    <th scope="col">Course Title</th>
                    <th scope="col">Description</th>
                  </tr>
                </thead>
                <tbody>
                  <template v-if="filteredCourses && filteredCourses.length > 0">
                    <tr 
                      v-for="(course, index) in filteredCourses" 
                      :key="course.id || index" 
                      @click="selectCourse(index)" 
                      :class="{ 'table-active': selectedCourse == index }"
                      style="cursor: pointer;"
                    >
                      <td class="text-center fw-bold text-muted">{{ course.id }}</td>
                      <td class="fw-semibold">{{ course.title }}</td>
                      <td class="text-muted">{{ course.description }}</td>
                    </tr>
                  </template>
                  <tr v-else-if="searchQuery && filteredCourses.length === 0">
                    <td colspan="3" class="text-center text-muted py-5">
                      <div>
                        <p class="mb-1"><em>No courses found matching "{{ searchQuery }}"</em></p>
                        <small class="text-muted">Try adjusting your search terms</small>
                      </div>
                    </td>
                  </tr>
                  <tr v-else-if="!filteredCourses || filteredCourses.length === 0">
                    <td colspan="3" class="text-center text-muted py-5">
                      <div>
                        <p class="mb-0"><em>No courses available</em></p>
                      </div>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>