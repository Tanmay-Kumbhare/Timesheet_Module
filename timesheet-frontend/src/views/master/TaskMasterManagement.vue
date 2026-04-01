<template>
  <div class="task-master-container">
    <div class="header-actions">
      <h2>Task Management</h2>
      <button @click="openAddModal" class="btn-add">+ New Task</button>
    </div>

    <div v-if="loading" class="loading">Loading tasks...</div>
    <div v-if="error" class="error-message">{{ error }}</div>

    <table v-else-if="taskMasters.length > 0" class="data-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Task Title</th>
          <th>Category</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in taskMasters" :key="item.id">
          <td>{{ item.id }}</td>
          <td>{{ item.name }}</td>
          <td>{{ item.taskType?.name || '-' }}</td>
          <td>
            <button @click="openEditModal(item)" class="btn-edit">Edit</button>
            <button @click="deleteItem(item.id)" class="btn-delete">Remove</button>
          </td>
        </tr>
      </tbody>
    </table>

    <div v-else-if="!loading && taskMasters.length === 0" class="no-data">
      No tasks found. Click "New Task" to create one.
    </div>

    <!-- Add/Edit Modal -->
    <div v-if="showModal" class="modal" @click.self="closeModal">
      <div class="modal-content">
        <h3>{{ isEdit ? 'Edit Task' : 'Add New Task' }}</h3>
        <form @submit.prevent="saveItem">
          <div class="form-group">
            <label>Task Title *</label>
            <input v-model="formData.name" required />
          </div>
          <div class="form-group">
            <label>Category *</label>
            <select v-model="formData.taskTypeId" required>
              <option value="">Select Category</option>
              <option v-for="type in taskTypes" :key="type.id" :value="type.id">
                {{ type.name }}
              </option>
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
  name: 'TaskMasterManagement',
  data() {
    return {
      taskMasters: [],
      taskTypes: [],
      loading: false,
      error: null,
      showModal: false,
      isEdit: false,
      formData: {
        id: null,
        name: '',
        taskTypeId: ''
      }
    }
  },
  mounted() {
    this.fetchTaskMasters()
    this.fetchTaskTypes()
  },
  methods: {
    async fetchTaskMasters() {
      this.loading = true
      this.error = null
      try {
        const response = await axios.get('http://localhost:8080/taskMasters')
        console.log('Task Masters response:', response.data)
        this.taskMasters = response.data.data || response.data || []
      } catch (err) {
        console.error('Error fetching tasks:', err)
        this.error = 'Failed to load tasks: ' + (err.message || 'Unknown error')
      } finally {
        this.loading = false
      }
    },
    async fetchTaskTypes() {
      try {
        const response = await axios.get('http://localhost:8080/taskTypes')
        console.log('Task Types response:', response.data)
        this.taskTypes = response.data.data || response.data || []
      } catch (err) {
        console.error('Error fetching task types:', err)
      }
    },
    openAddModal() {
      this.isEdit = false
      this.formData = { id: null, name: '', taskTypeId: '' }
      this.showModal = true
    },
    openEditModal(item) {
      this.isEdit = true
      this.formData = {
        id: item.id,
        name: item.name,
        taskTypeId: item.taskType?.id || ''
      }
      this.showModal = true
    },
    async saveItem() {
      if (!this.formData.name || !this.formData.taskTypeId) {
        alert('Please fill all required fields')
        return
      }
      
      const payload = {
        name: this.formData.name,
        taskTypeId: parseInt(this.formData.taskTypeId)
      }
      
      console.log('Saving payload:', payload)
      
      try {
        if (this.isEdit) {
          const response = await axios.put(`http://localhost:8080/taskMasters/${this.formData.id}`, payload)
          console.log('Update response:', response.data)
          alert('Task updated successfully')
        } else {
          const response = await axios.post('http://localhost:8080/taskMasters', payload)
          console.log('Create response:', response.data)
          alert('Task added successfully')
        }
        this.closeModal()
        this.fetchTaskMasters()
      } catch (err) {
        console.error('Save error:', err)
        let errorMsg = 'Failed to save task.\n'
        if (err.response?.data?.message) {
          errorMsg += err.response.data.message
        } else if (err.response?.data?.errors) {
          errorMsg += JSON.stringify(err.response.data.errors)
        } else {
          errorMsg += err.message
        }
        alert(errorMsg)
      }
    },
    async deleteItem(id) {
      if (confirm('Are you sure you want to remove this task?')) {
        try {
          await axios.delete(`http://localhost:8080/taskMasters/${id}`)
          alert('Task removed successfully')
          this.fetchTaskMasters()
        } catch (err) {
          console.error('Delete error:', err)
          alert('Failed to remove task: ' + (err.response?.data?.message || err.message))
        }
      }
    },
    closeModal() {
      this.showModal = false
      this.formData = { id: null, name: '', taskTypeId: '' }
    }
  }
}
</script>

<style scoped>
.task-master-container { padding: 20px; }
.header-actions { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.btn-add { background: #4caf50; color: white; padding: 10px 20px; border: none; border-radius: 4px; cursor: pointer; }
.btn-edit { background: #2196f3; color: white; margin-right: 8px; padding: 5px 10px; border: none; border-radius: 3px; cursor: pointer; }
.btn-delete { background: #f44336; color: white; padding: 5px 10px; border: none; border-radius: 3px; cursor: pointer; }
.data-table { width: 100%; border-collapse: collapse; background: white; }
.data-table th, .data-table td { border: 1px solid #ddd; padding: 12px; text-align: left; }
.data-table th { background: #1a237e; color: white; }
.loading { text-align: center; padding: 40px; }
.error-message { background: #ffebee; color: #c62828; padding: 15px; margin-bottom: 20px; border-left: 4px solid #c62828; }
.no-data { text-align: center; padding: 40px; color: #666; }
.modal { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 1000; }
.modal-content { background: white; padding: 30px; border-radius: 8px; width: 500px; max-width: 90%; }
.form-group { margin-bottom: 15px; }
.form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
.form-group input, .form-group select { width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; }
.modal-actions { display: flex; gap: 10px; justify-content: flex-end; margin-top: 20px; }
.btn-save { background: #4caf50; color: white; padding: 8px 16px; border: none; border-radius: 4px; cursor: pointer; }
.btn-cancel { background: #999; color: white; padding: 8px 16px; border: none; border-radius: 4px; cursor: pointer; }
</style>