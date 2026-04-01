<template>
  <div class="task-type-container">
    <div class="header-actions">
      <h2>Task Categories</h2>
      <button @click="openAddModal" class="btn-add">+ New Category</button>
    </div>

    <div v-if="loading" class="loading">
      <div class="spinner"></div>
      Loading...
    </div>

    <div v-if="error" class="error-message">
      {{ error }}
      <button @click="fetchTaskTypes" class="btn-retry">Retry</button>
    </div>

    <table v-else-if="taskTypes.length > 0" class="data-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Category Name</th>
          <th>Status</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in taskTypes" :key="item.id">
          <td>{{ item.id }}</td>
          <td>{{ item.name }}</td>
          <td>
            <span :class="'status-' + (item.isActive ? 'Active' : 'Inactive')">
              {{ item.isActive ? 'Active' : 'Inactive' }}
            </span>
          </td>
          <td>
            <button @click="openEditModal(item)" class="btn-edit">Edit</button>
            <button @click="deleteItem(item.id)" class="btn-delete">Remove</button>
          </td>
        </tr>
        <tr v-if="taskTypes.length === 0">
          <td colspan="4" class="no-data">No categories found</td>
        </tr>
      </tbody>
    </table>

    <!-- Modal -->
    <div v-if="showModal" class="modal" @click.self="closeModal">
      <div class="modal-content">
        <h3>{{ isEdit ? 'Edit Category' : 'Add New Category' }}</h3>
        <form @submit.prevent="saveItem">
          <div class="form-group">
            <label>Category Name *</label>
            <input v-model="formData.name" required autofocus />
          </div>
          <div class="form-group">
            <label>Status</label>
            <select v-model="formData.isActive">
              <option :value="true">Active</option>
              <option :value="false">Inactive</option>
            </select>
          </div>
          <div class="modal-actions">
            <button type="submit" class="btn-save">Save</button>
            <button type="button" @click="closeModal" class="btn-cancel">Cancel</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'TaskTypeManagement',
  data() {
    return {
      taskTypes: [],
      loading: false,
      error: null,
      showModal: false,
      isEdit: false,
      formData: {
        id: null,
        name: '',
        isActive: true
      }
    }
  },
  mounted() {
    this.fetchTaskTypes()
  },
  methods: {
    async fetchTaskTypes() {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get('http://localhost:8080/taskTypes')
        console.log('Task Types raw response:', response.data)
        
        if (response.data && response.data.data) {
          this.taskTypes = response.data.data
        } else if (Array.isArray(response.data)) {
          this.taskTypes = response.data
        } else {
          this.taskTypes = []
        }
        
        console.log('Task types loaded:', this.taskTypes.length)
      } catch (error) {
        console.error('Error fetching task types:', error)
        this.error = 'Failed to load task types. Make sure backend is running on port 8080'
      } finally {
        this.loading = false
      }
    },
    openAddModal() {
      this.isEdit = false
      this.formData = { id: null, name: '', isActive: true }
      this.showModal = true
    },
    openEditModal(item) {
      this.isEdit = true
      this.formData = { ...item }
      this.showModal = true
    },
    async saveItem() {
      if (!this.formData.name.trim()) {
        alert('Category name is required')
        return
      }
      
      const payload = {
        name: this.formData.name,
        isActive: this.formData.isActive
      }
      
      try {
        if (this.isEdit) {
          await axios.put(`http://localhost:8080/taskTypes/${this.formData.id}`, payload)
          alert('Category updated successfully')
        } else {
          await axios.post('http://localhost:8080/taskTypes', payload)
          alert('Category added successfully')
        }
        this.closeModal()
        this.fetchTaskTypes()
      } catch (error) {
        console.error('Error saving category:', error)
        const errorMsg = error.response?.data?.message || error.message
        alert(`Failed to save category: ${errorMsg}`)
      }
    },
    async deleteItem(id) {
      if (confirm('Are you sure you want to remove this category?')) {
        try {
          await axios.delete(`http://localhost:8080/taskTypes/${id}`)
          alert('Category removed successfully')
          this.fetchTaskTypes()
        } catch (error) {
          console.error('Error deleting category:', error)
          alert('Failed to remove category')
        }
      }
    },
    closeModal() {
      this.showModal = false
      this.formData = { id: null, name: '', isActive: true }
    }
  }
}
</script>

<style scoped>
.task-type-container { padding: 20px; }
.header-actions { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.btn-add { background: #4caf50; color: white; padding: 10px 20px; border: none; border-radius: 4px; cursor: pointer; font-size: 14px; }
.btn-add:hover { background: #45a049; }
.btn-edit { background: #2196f3; color: white; margin-right: 8px; padding: 5px 10px; border: none; border-radius: 3px; cursor: pointer; }
.btn-edit:hover { background: #1976d2; }
.btn-delete { background: #f44336; color: white; padding: 5px 10px; border: none; border-radius: 3px; cursor: pointer; }
.btn-delete:hover { background: #d32f2f; }
.btn-retry { background: #ff9800; color: white; padding: 5px 15px; border: none; border-radius: 3px; cursor: pointer; margin-left: 10px; }
.data-table { width: 100%; border-collapse: collapse; background: white; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
.data-table th, .data-table td { border: 1px solid #ddd; padding: 12px; text-align: left; }
.data-table th { background: #1a237e; color: white; }
.status-Active { color: green; font-weight: bold; }
.status-Inactive { color: red; }
.loading { text-align: center; padding: 40px; font-size: 18px; color: #666; }
.spinner { display: inline-block; width: 20px; height: 20px; border: 2px solid #ccc; border-top-color: #1a237e; border-radius: 50%; animation: spin 0.6s linear infinite; margin-right: 10px; vertical-align: middle; }
@keyframes spin { to { transform: rotate(360deg); } }
.error-message { background: #ffebee; color: #c62828; padding: 15px; border-radius: 4px; margin-bottom: 20px; border-left: 4px solid #c62828; }
.no-data { text-align: center; color: #666; padding: 40px; }
.modal { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 1000; }
.modal-content { background: white; padding: 30px; border-radius: 8px; width: 500px; max-width: 90%; box-shadow: 0 4px 20px rgba(0,0,0,0.2); }
.modal-content h3 { margin-bottom: 20px; color: #1a237e; }
.form-group { margin-bottom: 15px; }
.form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
.form-group input, .form-group select { width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; font-size: 14px; }
.form-group input:focus, .form-group select:focus { outline: none; border-color: #1a237e; }
.modal-actions { display: flex; gap: 10px; justify-content: flex-end; margin-top: 20px; }
.btn-save { background: #4caf50; color: white; padding: 8px 16px; border: none; border-radius: 4px; cursor: pointer; }
.btn-cancel { background: #999; color: white; padding: 8px 16px; border: none; border-radius: 4px; cursor: pointer; }
</style>