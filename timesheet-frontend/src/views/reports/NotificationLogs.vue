<template>
  <div class="logs-container">
    <div class="filter-section">
      <h2>Notification Logs</h2>
      <div class="filters">
        <input type="date" v-model="filters.startDate" />
        <input type="date" v-model="filters.endDate" />
        <select v-model="filters.status">
          <option value="">All Status</option>
          <option value="Sent">Sent</option>
          <option value="Failed">Failed</option>
          <option value="Pending">Pending</option>
        </select>
        <button @click="fetchLogs" class="btn-search">Search</button>
      </div>
    </div>

    <div v-if="loading" class="loading">Loading logs...</div>

    <table v-else class="logs-table">
      <thead>
        <tr><th>Date & Time</th><th>Event Type</th><th>Recipient</th><th>Channel</th><th>Status</th><th>Message</th></tr>
      </thead>
      <tbody>
        <tr v-for="(log, idx) in logs" :key="idx">
          <td>{{ log.timestamp }}</td>
          <td>{{ log.eventType }}</td>
          <td>{{ log.recipient }}</td>
          <td>{{ log.channel }}</td>
          <td><span :class="'status-' + log.status">{{ log.status }}</span></td>
          <td>{{ log.message }}</td>
        </tr>
        <tr v-if="logs.length === 0">
          <td colspan="6" class="no-data">No logs found</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import { reportApi } from '@/api'

export default {
  data() {
    return {
      filters: {
        startDate: '',
        endDate: '',
        status: ''
      },
      logs: [],
      loading: false
    }
  },
  mounted() {
    this.fetchLogs()
  },
  methods: {
    async fetchLogs() {
      this.loading = true
      try {
        const response = await reportApi.getNotificationLogs(this.filters)
        this.logs = response.data.data || response.data
      } catch (error) {
        console.error('Error fetching logs:', error)
        alert('Failed to load notification logs')
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.logs-container { padding: 20px; }
.filter-section { background: white; padding: 20px; border-radius: 8px; margin-bottom: 20px; }
.filters { display: flex; gap: 10px; flex-wrap: wrap; margin-top: 15px; }
.filters select, .filters input { padding: 8px; border: 1px solid #ddd; border-radius: 4px; }
.btn-search { background: #1a237e; color: white; padding: 8px 20px; border: none; border-radius: 4px; cursor: pointer; }
.logs-table { width: 100%; border-collapse: collapse; background: white; }
.logs-table th, .logs-table td { border: 1px solid #ddd; padding: 12px; text-align: left; }
.logs-table th { background: #1a237e; color: white; }
.status-Sent { color: green; font-weight: bold; }
.status-Failed { color: red; }
.status-Pending { color: orange; }
.loading { text-align: center; padding: 40px; }
.no-data { text-align: center; color: #666; }
</style>