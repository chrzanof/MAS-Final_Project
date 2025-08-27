<script>
export default {
  emits: ['close']
}
</script>

<template>
    <div class="modal fade show" style="display: block;" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="false">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5" id="staticBackdropLabel">Create New Course</h1>
        <button type="button" class="btn-close" @click="$emit('close')" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <form>
          <div class="mb-3">
            <label for="courseTitle" class="form-label">Course Title</label>
            <input type="text" class="form-control" id="courseTitle" placeholder="Enter course title">
          </div>
          <div class="mb-3">
            <label for="courseDescription" class="form-label">Course Description</label>
            <textarea class="form-control" id="courseDescription" rows="3" placeholder="Enter course description"></textarea>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" @click="$emit('close')">Cancel</button>
        <button type="button" class="btn btn-primary">Create Course</button>
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