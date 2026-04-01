<template>
  <div class="hod-dashboard">
    <h2>HOD Dashboard - Timesheet Approvals</h2>
    
    <!-- Department Filter -->
    <div class="filter-bar">
      <select v-model="selectedDepartment" @change="fetchTimesheets">
        <option value="">All Departments</option>
        <option v-for="dept in departments" :key="dept.id" :value="dept.id">
          {{ dept.name }}
        </option>
      </select>
      <select v-model="statusFilter" @change="fetchTimesheets">
        <option value="PENDING">Pending</option>
        <option value="APPROVED">Approved</option>
        <option value="REJECTED">Rejected</option>
        <option value="">All</option>
      </select>
    </div>

    <!-- Timesheets Table -->
    <div v-if="loading" class="loading">Loading...</div>
    <div v-else-if="timesheets.length === 0" class="no-data">
      No timesheets found
    </div>
    <table v-else class="timesheet-table">
      <thead>
        <tr>
          <th>Employee</th>
          <th>Department</th>
          <th>Date</th>
          <th>Task</th>
          <th>Hours</th>
          <th>Description</th>
          <th>Status</th>
          <th>Action</th>
         </tr>
      </thead>
      <tbody>
        <tr v-for="ts in timesheets" :key="ts.id">
          <td>{{ ts.employeeName }}</td>
          <td>{{ ts.department }}</td>
          <td>{{ ts.taskDate }}</td>
          <td>{{ ts.taskName }}</td>
          <td>{{ ts.totalHours }}</td>
          <td>{{ truncateText(ts.description) }}</td>
          <td>
            <span :class="'status-' + ts.status">
              {{ ts.status }}
            </span>
          </td>
          <td>
            <div v-if="ts.status === 'PENDING'">
              <button @click="approveTimesheet(ts.id)" class="btn-approve">Approve</button>
              <button @click="openRejectModal(ts.id)" class="btn-reject">Reject</button>
            </div>
            <div v-else>-</div>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Reject Modal -->
    <div v-if="showRejectModal" class="modal" @click.self="closeRejectModal">
      <div class="modal-content">
        <h3>Reject Timesheet</h3>
        <textarea v-model="rejectReason" placeholder="Enter reason for rejection..." rows="3"></textarea>
        <div class="modal-actions">
          <button @click="confirmReject" class="btn-reject">Confirm Reject</button>
          <button @click="closeRejectModal" class="btn-cancel">Cancel</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'HodDashboard',
  data() {
    return {
      timesheets: [],
      departments: [],
      selectedDepartment: '',
      statusFilter: 'PENDING',
      loading: false,
      showRejectModal: false,
      rejectTimesheetId: null,
      rejectReason: ''
    }
  },
  mounted() {
    this.fetchDepartments()
    this.fetchTimesheets()
  },
  methods: {
    truncateText(text) {
      if (!text) return ''
      if (text.length > 50) return text.substring(0, 50) + '...'
      return text
    },
    async fetchDepartments() {
      try {
        const response = await axios.get('http://localhost:8080/departments')
        this.departments = response.data.data || response.data || []
      } catch (error) {
        console.error('Error fetching departments:', error)
      }
    },
    async fetchTimesheets() {
      this.loading = true
      try {
        let url = 'http://localhost:8080/timesheets/all'
        const params = []
        if (this.selectedDepartment) params.push(`departmentId=${this.selectedDepartment}`)
        if (this.statusFilter) params.push(`status=${this.statusFilter}`)
        if (params.length) url += '?' + params.join('&')
        
        const response = await axios.get(url)
        this.timesheets = response.data.data || response.data || []
        console.log('Timesheets loaded:', this.timesheets.length)
      } catch (error) {
        console.error('Error fetching timesheets:', error)
        alert('Failed to load timesheets')
      } finally {
        this.loading = false
      }
    },
    async approveTimesheet(id) {
      if (confirm('Approve this timesheet?')) {
        try {
          const response = await axios.post('http://localhost:8080/timesheets/approve', null, {
            params: {
              id: id,
              status: 'APPROVED',
              remarks: 'Approved by HOD'
            }
          })
          console.log('Approve response:', response.data)
          alert('Timesheet approved successfully')
          this.fetchTimesheets()
        } catch (error) {
          console.error('Error approving timesheet:', error)
          alert('Failed to approve timesheet: ' + (error.response?.data?.message || error.message))
        }
      }
    },
    openRejectModal(id) {
      this.rejectTimesheetId = id
      this.rejectReason = ''
      this.showRejectModal = true
    },
    async confirmReject() {
      try {
        const response = await axios.post('http://localhost:8080/timesheets/approve', null, {
          params: {
            id: this.rejectTimesheetId,
            status: 'REJECTED',
            remarks: this.rejectReason || 'Rejected by HOD'
          }
        })
        console.log('Reject response:', response.data)
        alert('Timesheet rejected')
        this.closeRejectModal()
        this.fetchTimesheets()
      } catch (error) {
        console.error('Error rejecting timesheet:', error)
        alert('Failed to reject timesheet: ' + (error.response?.data?.message || error.message))
      }
    },
    closeRejectModal() {
      this.showRejectModal = false
      this.rejectTimesheetId = null
      this.rejectReason = ''
    }
  }
}
</script>

<style scoped>
.hod-dashboard { padding: 20px; }
.filter-bar { display: flex; gap: 15px; margin-bottom: 20px; }
.filter-bar select { padding: 8px; border: 1px solid #ddd; border-radius: 4px; }
.timesheet-table { width: 100%; border-collapse: collapse; background: white; }
.timesheet-table th, .timesheet-table td { border: 1px solid #ddd; padding: 10px; text-align: left; }
.timesheet-table th { background: #1a237e; color: white; }
.status-PENDING { color: orange; font-weight: bold; }
.status-APPROVED { color: green; font-weight: bold; }
.status-REJECTED { color: red; font-weight: bold; }
.btn-approve { background: #4caf50; color: white; padding: 5px 10px; border: none; border-radius: 3px; cursor: pointer; margin-right: 5px; }
.btn-reject { background: #f44336; color: white; padding: 5px 10px; border: none; border-radius: 3px; cursor: pointer; }
.loading, .no-data { text-align: center; padding: 40px; color: #666; }
.modal { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 1000; }
.modal-content { background: white; padding: 30px; border-radius: 8px; width: 400px; max-width: 90%; }
.modal-content textarea { width: 100%; padding: 8px; margin: 10px 0; border: 1px solid #ddd; border-radius: 4px; }
.modal-actions { display: flex; gap: 10px; justify-content: flex-end; margin-top: 15px; }
.btn-cancel { background: #999; color: white; padding: 8px 16px; border: none; border-radius: 4px; cursor: pointer; }
</style>