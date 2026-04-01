<template>
  <div class="reports-container">
    <h2>Timesheet Reports</h2>
    
    <!-- Filter Section -->
    <div class="filter-section">
      <div class="filter-row">
        <div class="form-group">
          <label>Department</label>
          <select v-model="filters.departmentId">
            <option value="">All Departments</option>
            <option v-for="dept in departments" :key="dept.id" :value="dept.id">
              {{ dept.name }}
            </option>
          </select>
        </div>
        
        <div class="form-group">
          <label>Status</label>
          <select v-model="filters.status">
            <option value="">All</option>
            <option value="PENDING">Pending</option>
            <option value="APPROVED">Approved</option>
            <option value="REJECTED">Rejected</option>
          </select>
        </div>
        
        <div class="form-group">
          <label>From Date</label>
          <input type="date" v-model="filters.fromDate" />
        </div>
        
        <div class="form-group">
          <label>To Date</label>
          <input type="date" v-model="filters.toDate" />
        </div>
        
        <div class="form-group">
          <button @click="fetchReport" class="btn-search">Search</button>
          <button @click="exportReport" class="btn-export">Export</button>
        </div>
      </div>
    </div>
    
    <!-- Summary Cards -->
    <div class="summary-cards">
      <div class="card">
        <h3>Total Timesheets</h3>
        <p class="number">{{ summary.total }}</p>
      </div>
      <div class="card pending">
        <h3>Pending</h3>
        <p class="number">{{ summary.pending }}</p>
      </div>
      <div class="card approved">
        <h3>Approved</h3>
        <p class="number">{{ summary.approved }}</p>
      </div>
      <div class="card rejected">
        <h3>Rejected</h3>
        <p class="number">{{ summary.rejected }}</p>
      </div>
    </div>
    
    <!-- Report Table -->
    <div v-if="loading" class="loading">Loading report...</div>
    <div v-else-if="reportData.length === 0" class="no-data">
      No timesheet data found
    </div>
    <table v-else class="report-table">
      <thead>
        <tr>
          <th>Employee</th>
          <th>Department</th>
          <th>Date</th>
          <th>Task</th>
          <th>Hours</th>
          <th>Status</th>
          <th>Submitted</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in reportData" :key="item.id">
          <td>{{ item.employeeName }}</td>
          <td>{{ item.department }}</td>
          <td>{{ item.taskDate }}</td>
          <td>{{ item.taskName }}</td>
          <td>{{ item.totalHours }}</td>
          <td>
            <span :class="'status-' + item.status">
              {{ item.status }}
            </span>
          </td>
          <td>{{ item.submittedDate }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'TimesheetReports',
  data() {
    return {
      departments: [],
      reportData: [],
      loading: false,
      filters: {
        departmentId: '',
        status: '',
        fromDate: '',
        toDate: ''
      },
      summary: {
        total: 0,
        pending: 0,
        approved: 0,
        rejected: 0
      }
    }
  },
  mounted() {
    this.fetchDepartments()
    this.fetchReport()
  },
  methods: {
    async fetchDepartments() {
      try {
        const response = await axios.get('http://localhost:8080/departments')
        this.departments = response.data.data || response.data || []
      } catch (error) {
        console.error('Error fetching departments:', error)
      }
    },
    async fetchReport() {
      this.loading = true
      try {
        let url = 'http://localhost:8080/timesheets/all'
        const params = []
        if (this.filters.departmentId) params.push(`departmentId=${this.filters.departmentId}`)
        if (this.filters.status) params.push(`status=${this.filters.status}`)
        if (this.filters.fromDate) params.push(`fromDate=${this.filters.fromDate}`)
        if (this.filters.toDate) params.push(`toDate=${this.filters.toDate}`)
        if (params.length) url += '?' + params.join('&')
        
        const response = await axios.get(url)
        this.reportData = response.data.data || response.data || []
        
        // Calculate summary
        this.summary.total = this.reportData.length
        this.summary.pending = this.reportData.filter(t => t.status === 'PENDING').length
        this.summary.approved = this.reportData.filter(t => t.status === 'APPROVED').length
        this.summary.rejected = this.reportData.filter(t => t.status === 'REJECTED').length
        
      } catch (error) {
        console.error('Error fetching report:', error)
        alert('Failed to load report data')
      } finally {
        this.loading = false
      }
    },
    exportReport() {
      // Create CSV
      const headers = ['Employee', 'Department', 'Date', 'Task', 'Hours', 'Status', 'Submitted']
      const rows = this.reportData.map(item => [
        item.employeeName,
        item.department,
        item.taskDate,
        item.taskName,
        item.totalHours,
        item.status,
        item.submittedDate
      ])
      
      const csvContent = [headers, ...rows].map(row => row.join(',')).join('\n')
      const blob = new Blob([csvContent], { type: 'text/csv' })
      const url = window.URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = `timesheet_report_${new Date().toISOString().split('T')[0]}.csv`
      a.click()
      window.URL.revokeObjectURL(url)
      
      alert('Report exported as CSV')
    }
  }
}
</script>

<style scoped>
.reports-container { padding: 20px; }
.reports-container h2 { color: #1a237e; margin-bottom: 20px; }
.filter-section { background: white; padding: 20px; border-radius: 8px; margin-bottom: 20px; }
.filter-row { display: flex; gap: 15px; flex-wrap: wrap; align-items: flex-end; }
.form-group { display: flex; flex-direction: column; gap: 5px; }
.form-group label { font-weight: 600; font-size: 12px; color: #666; }
.form-group select, .form-group input { padding: 8px; border: 1px solid #ddd; border-radius: 4px; min-width: 150px; }
.btn-search { background: #1a237e; color: white; padding: 8px 20px; border: none; border-radius: 4px; cursor: pointer; }
.btn-export { background: #4caf50; color: white; padding: 8px 20px; border: none; border-radius: 4px; cursor: pointer; margin-left: 10px; }
.summary-cards { display: grid; grid-template-columns: repeat(4, 1fr); gap: 15px; margin-bottom: 20px; }
.card { background: white; padding: 15px; border-radius: 8px; text-align: center; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
.card h3 { font-size: 14px; color: #666; margin-bottom: 10px; }
.card .number { font-size: 28px; font-weight: bold; color: #333; }
.card.pending .number { color: orange; }
.card.approved .number { color: green; }
.card.rejected .number { color: red; }
.report-table { width: 100%; border-collapse: collapse; background: white; }
.report-table th, .report-table td { border: 1px solid #ddd; padding: 10px; text-align: left; }
.report-table th { background: #1a237e; color: white; }
.status-PENDING { color: orange; font-weight: bold; }
.status-APPROVED { color: green; font-weight: bold; }
.status-REJECTED { color: red; font-weight: bold; }
.loading, .no-data { text-align: center; padding: 40px; color: #666; }
</style>