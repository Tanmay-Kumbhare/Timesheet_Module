<template>
  <div class="report-container">
    <div class="filter-section">
      <h2>Institutewise Timesheet Report</h2>
      <div class="filters">
        <select v-model="filters.institutionId">
          <option value="">Select Institution</option>
          <option v-for="inst in institutions" :key="inst.id" :value="inst.id">{{ inst.name }}</option>
        </select>
        <input type="month" v-model="filters.month" />
        <button @click="fetchReport" class="btn-search">Search</button>
        <button @click="exportReport" class="btn-export">Export</button>
      </div>
    </div>

    <div v-if="loading" class="loading">Loading report...</div>

    <div v-else-if="reportData" class="report-content">
      <table class="report-table">
        <thead>
          <tr><th>Institution</th><th>Department</th><th>Total Employees</th><th>Total Hours</th><th>Approved Hours</th><th>Pending Hours</th></tr>
        </thead>
        <tbody>
          <tr v-for="(item, idx) in reportData" :key="idx">
            <td>{{ item.institutionName }}</td>
            <td>{{ item.departmentName }}</td>
            <td>{{ item.totalEmployees }}</td>
            <td>{{ item.totalHours }}</td>
            <td>{{ item.approvedHours }}</td>
            <td>{{ item.pendingHours }}</td>
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
  data() {
    return {
      institutions: [
        { id: 1, name: 'VIT Pune' },
        { id: 2, name: 'COEP Pune' },
        { id: 3, name: 'PICT Pune' }
      ],
      filters: {
        institutionId: '',
        month: ''
      },
      reportData: null,
      loading: false
    }
  },
  methods: {
    async fetchReport() {
      if (!this.filters.institutionId) {
        alert('Please select an institution')
        return
      }
      this.loading = true
      try {
        const response = await reportApi.getInstitutewise(this.filters)
        this.reportData = response.data.data || response.data
      } catch (error) {
        console.error('Error fetching report:', error)
        alert('Failed to load report')
      } finally {
        this.loading = false
      }
    },
    exportReport() {
      alert('Export functionality')
    }
  }
}
</script>

<style scoped>
/* Same styles as DepartmentwiseReport */
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
</style>