<template>
  <div>
    <h3 class="section-title">Notification Logs</h3>

    <div class="filter-card">
      <div class="filter-grid">
        <div class="filter-row">
          <label>Status</label>
          <select v-model="filters.status">
            <option value="">-- All --</option>
            <option value="SENT">Sent</option>
            <option value="FAILED">Failed</option>
          </select>
        </div>
        <div class="filter-row">
          <label>Date From</label>
          <input type="date" v-model="filters.dateFrom" />
        </div>
        <div class="filter-row">
          <label>Date To</label>
          <input type="date" v-model="filters.dateTo" />
        </div>
      </div>
      <div class="filter-actions">
        <button class="btn-search" @click="loadLogs">Search</button>
        <button class="btn-clear" @click="clearFilters">Clear</button>
      </div>
    </div>

    <table v-if="logs.length > 0">
      <thead>
        <tr>
          <th>Sr. No.</th>
          <th>Emp No</th>
          <th>Employee</th>
          <th>Department</th>
          <th>Subject</th>
          <th>Status</th>
          <th>Sent At</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(log, idx) in logs" :key="log.logId">
          <td>{{ idx + 1 }}</td>
          <td>{{ log.empNo }}</td>
          <td>{{ log.employeeName }}</td>
          <td>{{ log.departmentName }}</td>
          <td>{{ log.subject }}</td>
          <td>
            <span :class="log.status === 'SENT' ? 'badge-sent' : 'badge-failed'">
              {{ log.status }}
            </span>
          </td>
          <td>{{ log.sentAt }}</td>
        </tr>
      </tbody>
    </table>
    <p v-else class="hint">Select filters and click Search, or no logs available yet.</p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { reportApi } from '@/api/index.js'

const logs    = ref([])
const filters = ref({ status: '', dateFrom: '', dateTo: '' })

async function loadLogs() {
  const params = {}
  if (filters.value.status)   params.status   = filters.value.status
  if (filters.value.dateFrom) params.dateFrom = filters.value.dateFrom
  if (filters.value.dateTo)   params.dateTo   = filters.value.dateTo
  logs.value = await reportApi.notificationLogs(params)
}

function clearFilters() {
  filters.value = { status: '', dateFrom: '', dateTo: '' }
  logs.value = []
}
</script>

<style scoped>
.section-title { margin-bottom: 14px; font-size: 15px; font-weight: bold; color: #1a237e; }
.filter-card { border: 1px solid #ccc; padding: 14px; border-radius: 4px; margin-bottom: 16px; background: #fafafa; }
.filter-grid { display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 12px; margin-bottom: 12px; }
.filter-row { display: flex; flex-direction: column; gap: 4px; }
.filter-row label { font-weight: 500; font-size: 13px; }
.filter-actions { display: flex; gap: 10px; }
.btn-search { background: #1a237e; color: #fff; border: none; padding: 7px 20px; border-radius: 3px; }
.btn-clear  { background: #aaa;    color: #fff; border: none; padding: 7px 20px; border-radius: 3px; }
.hint { color: #888; padding: 20px; text-align: center; }
.badge-sent   { background: #e8f5e9; color: #388e3c; padding: 2px 8px; border-radius: 10px; font-size: 12px; }
.badge-failed { background: #fce4ec; color: #c62828; padding: 2px 8px; border-radius: 10px; font-size: 12px; }
</style>
