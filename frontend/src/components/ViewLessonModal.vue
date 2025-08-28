<template>
  <div class="modal fade" id="viewLessonModal" tabindex="-1" aria-labelledby="viewLessonModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="viewLessonModalLabel">
            <i class="bi bi-book me-2"></i>
            Lesson Details
          </h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body" v-if="lesson">
          <div class="row">
            <div class="col-12">
              <div class="card border-0">
                <div class="card-header bg-success text-white">
                  <div class="d-flex justify-content-between align-items-center">
                    <h6 class="mb-0">{{ lesson.title }}</h6>
                    <span class="badge bg-light text-dark">Lesson {{ lesson.lessonNumber }}</span>
                  </div>
                </div>
                <div class="card-body">
                  <div class="mb-3" v-if="lesson.description">
                    <h6 class="text-muted mb-2">Description</h6>
                    <p class="mb-0">{{ lesson.description }}</p>
                  </div>
                  
                  <div v-if="lesson.content">
                    <h6 class="text-muted mb-2">Content</h6>
                    <div class="p-3 bg-light rounded">
                      <div style="white-space: pre-wrap; line-height: 1.6;">{{ lesson.content }}</div>
                    </div>
                  </div>
                  
                  <div v-else class="text-center text-muted py-4">
                    <i class="bi bi-file-text-fill display-4 mb-3"></i>
                    <p class="mb-0">No content available for this lesson</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ViewLessonModal',
  props: {
    lesson: {
      type: Object,
      default: null
    }
  }
}
</script>

<style scoped>
.modal-dialog {
  max-width: 800px;
}

.card-header h6 {
  font-size: 1.1rem;
  font-weight: 600;
}
</style>
