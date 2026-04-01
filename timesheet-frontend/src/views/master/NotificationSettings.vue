<template>
  <div class="notification-container">
    <div class="header-actions">
      <h2>Notification Settings</h2>
      <button @click="showAddModal = true" class="btn-add">+ Add Setting</button>
    </div>

    <div v-if="loading" class="loading">Loading...</div>

    <table v-else class="data-table">
      <thead>
        <tr><th>ID</th><th>Notification Type</th><th>Recipient Role</th><th>Subject</th><th>Status</th><th>Actions</th></tr>
      </thead>
      <tbody>
        <tr v-for="item in notifications" :key="item.id">
          <td>{{ item.id }}</td>
          <td>{{ item.notificationType }}</td>
          <td>{{ item.recipientRole }}</td>
          <td>{{ item.subject }}</td>
          <td>
            <span :class="'status-' + (item.isActive ? 'Active' : 'Inactive')">
              {{ item.isActive ? 'Active' : 'Inactive' }}
            </span>
          </td>
          <td>
            <button @click="editItem(item)" class="btn-edit">Edit</button>
            <button @click="deleteItem(item.id)" class="btn-delete">Delete</button>
          </td>
        </tr>
        <tr v-if="notifications.length === 0">
          <td colspan="6" class="no-data">No notification settings found</td>
        </tr>
      </tbody>
    </table>

    <!-- Add/Edit Modal -->
    <div v-if="showAddModal || showEditModal" class="modal">
      <div class="modal-content">
        <h3>{{ showAddModal ? 'Add Notification Setting' : 'Edit Notification Setting' }}</h3>
        <form @submit.prevent="saveItem">
          <div class="form-group">
            <label>Notification Type *</label>
            <select v-model="formData.notificationType" required>
              <option value="EMAIL_REMINDER">Email Reminder</option>
              <option value="APPROVAL_ALERT">Approval Alert</option>
              <option value="TIMESHEET_SUBMISSION">Timesheet Submission</option>
              <option value="DEADLINE_REMINDER">Deadline Reminder</option>
            </select>
          </div>
          <div class="form-group">
            <label>Recipient Role *</label>
            <select v-model="formData.recipientRole" required>
              <option value="EMPLOYEE">Employee</option>
              <option value="HOD">HOD</option>
              <option value="ADMIN">Admin</option>
              <option value="COORDINATOR">Coordinator</option>
            </select>
          </div>
          <div class="form-group">
            <label>Subject *</label>
            <input v-model="formData.subject" required />
          </div>
          <div class="form-group">
            <label>Message Template *</label>
            <textarea v-model="formData.messageTemplate" rows="3" required></textarea>
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
import { notificationApi } from '@/api'

export default {
  data() {
    return {
      notifications: [],
      loading: false,
      showAddModal: false,
      showEditModal: false,
      formData: {
        id: null,
        notificationType: 'EMAIL_REMINDER',
        recipientRole: 'EMPLOYEE',
        subject: '',
        messageTemplate: '',
        isActive: true
      }
    }
  },
  mounted() {
    this.fetchNotifications()
  },
  methods: {
    async fetchNotifications() {
      this.loading = true
      try {
        const response = await notificationApi.getAll()
        this.notifications = response || []
        console.log('Notifications loaded:', this.notifications)
      } catch (error) {
        console.error('Error fetching notifications:', error)
        alert('Failed to load notification settings')
      } finally {
        this.loading = false
      }
    },
    editItem(item) {
      this.formData = { ...item }
      this.showEditModal = true
    },
    async saveItem() {
      if (!this.formData.subject || !this.formData.messageTemplate) {
        alert('Please fill all required fields')
        return
      }
      
      try {
        if (this.showAddModal) {
          await notificationApi.create(this.formData)
          alert('Notification setting added successfully')
        } else {
          await notificationApi.update(this.formData.id, this.formData)
          alert('Notification setting updated successfully')
        }
        this.closeModal()
        this.fetchNotifications()
      } catch (error) {
        console.error('Error saving notification:', error)
        alert('Failed to save notification setting: ' + (error.response?.data?.message || error.message))
      }
    },
    async deleteItem(id) {
      if (confirm('Are you sure you want to delete this notification setting?')) {
        try {
          await notificationApi.delete(id)
          alert('Notification setting deleted successfully')
          this.fetchNotifications()
        } catch (error) {
          console.error('Error deleting notification:', error)
          alert('Failed to delete notification setting')
        }
      }
    },
    closeModal() {
      this.showAddModal = false
      this.showEditModal = false
      this.formData = {
        id: null,
        notificationType: 'EMAIL_REMINDER',
        recipientRole: 'EMPLOYEE',
        subject: '',
        messageTemplate: '',
        isActive: true
      }
    }
  }
}
</script>

<style scoped>
.notification-container { padding: 20px; }
.header-actions { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.btn-add { background: #4caf50; color: white; padding: 10px 20px; border: none; border-radius: 4px; cursor: pointer; }
.btn-edit { background: #2196f3; color: white; margin-right: 8px; padding: 5px 10px; border: none; border-radius: 3px; cursor: pointer; }
.btn-delete { background: #f44336; color: white; padding: 5px 10px; border: none; border-radius: 3px; cursor: pointer; }
.data-table { width: 100%; border-collapse: collapse; }
.data-table th, .data-table td { border: 1px solid #ddd; padding: 12px; text-align: left; }
.data-table th { background: #1a237e; color: white; }
.status-Active { color: green; font-weight: bold; }
.status-Inactive { color: red; }
.loading { text-align: center; padding: 40px; }
.no-data { text-align: center; padding: 40px; color: #666; }
.modal { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 1000; }
.modal-content { background: white; padding: 30px; border-radius: 8px; width: 600px; max-width: 90%; }
.form-group { margin-bottom: 15px; }
.form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
.form-group input, .form-group select, .form-group textarea { width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; }
.modal-actions { display: flex; gap: 10px; justify-content: flex-end; margin-top: 20px; }
.btn-save { background: #4caf50; color: white; padding: 8px 16px; border: none; border-radius: 4px; cursor: pointer; }
.btn-cancel { background: #999; color: white; padding: 8px 16px; border: none; border-radius: 4px; cursor: pointer; }
</style>