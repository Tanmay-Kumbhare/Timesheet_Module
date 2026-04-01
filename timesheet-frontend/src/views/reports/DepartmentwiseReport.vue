<template>
  <div class="report-container">
    <div class="filter-section">
      <h2>Departmentwise Timesheet Report</h2>
      <div class="filters">
        <select v-model="filters.departmentId">
          <option value="">Select Department</option>
          <option v-for="dept in departments" :key="dept.id" :value="dept.id">{{ dept.name }}</option>
        </select>
        <input type="date" v-model="filters.startDate" placeholder="Start Date" />
        <input type="date" v-model="filters.endDate" placeholder="End Date" />
        <button @click="fetchReport" class="btn-search">Search</button>
        <button @click="exportReport" class="btn-export">Export</button>
      </div>
    </div>

    <div v-if="loading" class="loading">Loading report...</div>
    <div v-if="error" class="error-message">{{ error }}</div>

    <div v-else-if="reportData && reportData.length > 0" class="report-content">
      <table class="report-table">
        <thead>
          <tr><th>Employee Name</th><th>Department</th><th>Date</th><th>Task</th><th>Hours</th><th>Status</th></tr>
        </thead>
        <tbody>
          <tr v-for="(item, idx) in reportData" :key="idx">
            <td>{{ item.employeeName || '-' }}</td>
            <td>{{ item.departmentName || '-' }}</td>
            <td>{{ item.date || '-' }}</td>
            <td>{{ item.taskName || '-' }}</td>
            <td>{{ item.hours || '-' }}</td>
            <td>{{ item.status || '-' }}</td>
          </tr>
          <tr v-if="reportData.length === 0">
            <td colspan="6" class="no-data">No data found</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import { reportApi } from '@/api'

export default {
  name: 'DepartmentwiseReport',
  data() {
    return {
      departments: [
        { id: 1, name: 'Computer Engineering' },
        { id: 2, name: 'IT Engineering' },
        { id: 3, name: 'Electronics Engineering' },
        { id: 4, name: 'Mechanical Engineering' }
      ],
      filters: {
        departmentId: '',
        startDate: '',
        endDate: ''
      },
      reportData: null,
      loading: false,
      error: null
    }
  },
  methods: {
    async fetchReport() {
      if (!this.filters.departmentId) {
        alert('Please select a department')
        return
      }
      this.loading = true
      this.error = null
      try {
        const response = await reportApi.getDepartmentwise(this.filters)
        this.reportData = response || []
        console.log('Report data:', this.reportData)
      } catch (error) {
        console.error('Error fetching report:', error)
        this.error = 'Failed to load report: ' + (error.message || 'Unknown error')
      } finally {
        this.loading = false
      }
    },
    exportReport() {
      alert('Export functionality - will generate CSV/PDF')
    }
  }
}
</script>

<style scoped>
.report-container { padding: 20px; }
.filter-section { background: white; padding: 20px; border-radius: 8px; margin-bottom: 20px; }
.filters { display: flex; gap: 10px; flex-wrap: wrap; margin-top: 15px; }
.filters select, .filters input { padding: 8px; border: 1px solid #ddd; border-radius: 4px; min-width: 150px; }
.btn-search { background: #1a237e; color: white; padding: 8px 20px; border: none; border-radius: 4px; cursor: pointer; }
.btn-export { background: #4caf50; color: white; padding: 8px 20px; border: none; border-radius: 4px; cursor: pointer; }
.report-table { width: 100%; border-collapse: collapse; background: white; }
.report-table th, .report-table td { border: 1px solid #ddd; padding: 12px; text-align: left; }
.report-table th { background: #1a237e; color: white; }
.loading { text-align: center; padding: 40px; }
.error-message { background: #ffebee; color: #c62828; padding: 15px; border-radius: 4px; margin-bottom: 20px; border-left: 4px solid #c62828; }
.no-data { text-align: center; color: #666; padding: 40px; }
</style>