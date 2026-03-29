<template>
  <div>
    <h3 class="section-title">Timesheet Report – Departmentwise</h3>

    <!-- Filter Form (timesheetReportFilterForHOD equivalent) -->
    <div class="filter-card">
      <div class="filter-grid">
        <div class="filter-row">
          <label>Department</label>
          <select v-model="filters.departmentId" @change="loadEmployees">
            <option value="">-- All Departments --</option>
            <option v-for="d in departments" :key="d.id" :value="d.id">{{ d.name }}</option>
          </select>
        </div>
        <div class="filter-row">
          <label>Employee</label>
          <select v-model="filters.employeeId">
            <option value="">-- All Employees --</option>
            <option v-for="e in employees" :key="e.id" :value="e.id">{{ e.empNo }} - {{ e.name }}</option>
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
        <button class="btn-search" @click="loadReport">Search</button>
        <button class="btn-clear" @click="clearFilters">Clear</button>
      </div>
    </div>

    <!-- Report Table (dailytimesheetreportforHOD equivalent) -->
    <div v-if="reportData.length > 0 || searched">
      <p v-if="reportData.length === 0 && searched" class="no-data">No records found for the selected filters.</p>
      <table v-else>
        <thead>
          <tr>
            <th>Sr. No.</th>
            <th>Emp No</th>
            <th>Employee Name</th>
            <th>Department</th>
            <th>Task</th>
            <th>Task Type</th>
            <th>Date From</th>
            <th>Date To</th>
            <th>Hours Spent</th>
            <th>Description</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(row, idx) in reportData" :key="idx">
            <td>{{ idx + 1 }}</td>
            <td>{{ row.empNo }}</td>
            <td>{{ row.employeeName }}</td>
            <td>{{ row.departmentName }}</td>
            <td>{{ row.taskName }}</td>
            <td>{{ row.taskType }}</td>
            <td>{{ row.dateFrom }}</td>
            <td>{{ row.dateTo }}</td>
            <td>{{ row.hoursSpent }}</td>
            <td>{{ row.description }}</td>
          </tr>
          <tr class="total-row">
            <td colspan="8" style="text-align:right; font-weight:bold">Total Hours:</td>
            <td style="font-weight:bold">{{ totalHours }}</td>
            <td></td>
          </tr>
        </tbody>
      </table>
    </div>
    <p v-else class="hint">Select filters and click Search to view the report.</p>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { reportApi, refApi } from '@/api/index.js'

const departments = ref([])
const employees   = ref([])
const reportData  = ref([])
const searched    = ref(false)
const filters     = ref({ departmentId: '', employeeId: '', dateFrom: '', dateTo: '' })

onMounted(async () => {
  departments.value = await refApi.departments()
})

async function loadEmployees() {
  filters.value.employeeId = ''
  employees.value = filters.value.departmentId
    ? await refApi.employees(filters.value.departmentId)
    : []
}

async function loadReport() {
  const params = {}
  if (filters.value.departmentId) params.departmentId = filters.value.departmentId
  if (filters.value.employeeId)   params.employeeId   = filters.value.employeeId
  if (filters.value.dateFrom)     params.dateFrom     = filters.value.dateFrom
  if (filters.value.dateTo)       params.dateTo       = filters.value.dateTo
  reportData.value = await reportApi.departmentwise(params)
  searched.value = true
}

function clearFilters() {
  filters.value = { departmentId: '', employeeId: '', dateFrom: '', dateTo: '' }
  employees.value = []
  reportData.value = []
  searched.value = false
}

const totalHours = computed(() =>
  reportData.value.reduce((sum, r) => sum + (parseFloat(r.hoursSpent) || 0), 0).toFixed(2)
)
</script>

<style scoped>
.section-title { margin-bottom: 14px; font-size: 15px; font-weight: bold; color: #1a237e; }
.filter-card { border: 1px solid #ccc; padding: 14px; border-radius: 4px; margin-bottom: 16px; background: #fafafa; }
.filter-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; margin-bottom: 12px; }
.filter-row { display: flex; flex-direction: column; gap: 4px; }
.filter-row label { font-weight: 500; font-size: 13px; }
.filter-actions { display: flex; gap: 10px; }
.btn-search { background: #1a237e; color: #fff; border: none; padding: 7px 20px; border-radius: 3px; }
.btn-clear  { background: #aaa;    color: #fff; border: none; padding: 7px 20px; border-radius: 3px; }
.no-data, .hint { color: #888; padding: 20px; text-align: center; }
.total-row { background: #f5f5f5; }
</style>