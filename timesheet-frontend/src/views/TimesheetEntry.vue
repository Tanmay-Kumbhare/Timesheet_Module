<template>
  <div class="timesheet-entry">
    <div class="header">
      <h2>Submit Timesheet</h2>
      <p>Fill in your daily work details</p>
    </div>

    <form @submit.prevent="submitTimesheet" class="timesheet-form">
      <!-- Employee Selection -->
      <div class="form-group">
        <label>Employee *</label>
        <select v-model="selectedEmployeeId" required>
          <option value="">Select Employee</option>
          <option v-for="emp in employees" :key="emp.id" :value="emp.id">
            {{ emp.name }} ({{ emp.department?.name || 'No Dept' }})
          </option>
        </select>
      </div>

      <!-- Task Selection -->
      <div class="form-group">
        <label>Task *</label>
        <select v-model="formData.taskId" required>
          <option value="">Select a task</option>
          <option v-for="task in tasks" :key="task.id" :value="task.id">
            {{ task.name }} ({{ task.taskType?.name }})
          </option>
        </select>
      </div>

      <!-- Date and Time Row -->
      <div class="form-row">
        <div class="form-group">
          <label>Date *</label>
          <input type="date" v-model="formData.taskDate" required />
        </div>
        
        <div class="form-group">
          <label>Start Time *</label>
          <input type="time" v-model="formData.startTime" required @change="calculateHours" />
        </div>
        
        <div class="form-group">
          <label>End Time *</label>
          <input type="time" v-model="formData.endTime" required @change="calculateHours" />
        </div>
        
        <div class="form-group hours-display">
          <label>Total Hours</label>
          <div class="hours-badge">{{ calculatedHours }} hrs</div>
        </div>
      </div>

      <!-- Description -->
      <div class="form-group">
        <label>Description / Work Done *</label>
        <textarea 
          v-model="formData.description" 
          rows="5" 
          required
          placeholder="Describe what you worked on today..."
        ></textarea>
        <small class="char-count">{{ formData.description.length }}/1000 characters</small>
      </div>

      <!-- Submit Button -->
      <div class="form-actions">
        <button type="button" @click="resetForm" class="btn-reset">Reset</button>
        <button type="submit" class="btn-submit" :disabled="submitting">
          {{ submitting ? 'Submitting...' : 'Submit Timesheet' }}
        </button>
      </div>
    </form>

    <!-- Recent Timesheets -->
    <div class="recent-timesheets">
      <h3>My Recent Timesheets</h3>
      <div v-if="loading" class="loading">Loading...</div>
      <div v-else-if="myTimesheets.length === 0" class="no-data">
        No timesheets submitted yet
      </div>
      <table v-else class="timesheet-table">
        <thead>
          <tr>
            <th>Date</th>
            <th>Task</th>
            <th>Start</th>
            <th>End</th>
            <th>Hours</th>
            <th>Status</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="ts in myTimesheets" :key="ts.id">
            <td>{{ ts.taskDate }}</td>
            <td>{{ ts.taskName }}</td>
            <td>{{ ts.startTime }}</td>
            <td>{{ ts.endTime }}</td>
            <td>{{ ts.totalHours }}</td>
            <td>
              <span :class="'status-' + ts.status">
                {{ ts.status }}
              </span>
            </td>
            <td>
              <button @click="viewDetails(ts)" class="btn-view">View</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- View Details Modal -->
    <div v-if="showDetailsModal" class="modal" @click.self="closeModal">
      <div class="modal-content details-modal">
        <h3>Timesheet Details</h3>
        <div class="details-grid">
          <div class="detail-item">
            <label>Employee:</label>
            <span>{{ selectedTimesheet?.employeeName }}</span>
          </div>
          <div class="detail-item">
            <label>Date:</label>
            <span>{{ selectedTimesheet?.taskDate }}</span>
          </div>
          <div class="detail-item">
            <label>Task:</label>
            <span>{{ selectedTimesheet?.taskName }}</span>
          </div>
          <div class="detail-item">
            <label>Time:</label>
            <span>{{ selectedTimesheet?.startTime }} - {{ selectedTimesheet?.endTime }}</span>
          </div>
          <div class="detail-item">
            <label>Total Hours:</label>
            <span>{{ selectedTimesheet?.totalHours }} hrs</span>
          </div>
          <div class="detail-item full-width">
            <label>Description:</label>
            <p>{{ selectedTimesheet?.description }}</p>
          </div>
          <div class="detail-item">
            <label>Status:</label>
            <span :class="'status-' + selectedTimesheet?.status">{{ selectedTimesheet?.status }}</span>
          </div>
          <div class="detail-item" v-if="selectedTimesheet?.remarks">
            <label>Remarks:</label>
            <span>{{ selectedTimesheet?.remarks }}</span>
          </div>
        </div>
        <button @click="closeModal" class="btn-close">Close</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'TimesheetEntry',
  data() {
    return {
      employees: [],
      tasks: [],
      selectedEmployeeId: '',
      formData: {
        taskId: '',
        taskDate: new Date().toISOString().split('T')[0],
        startTime: '09:00',
        endTime: '17:00',
        description: ''
      },
      myTimesheets: [],
      loading: false,
      submitting: false,
      calculatedHours: 0,
      showDetailsModal: false,
      selectedTimesheet: null
    }
  },
  mounted() {
    this.fetchEmployees()
    this.fetchTasks()
    this.fetchMyTimesheets()
  },
  methods: {
    async fetchEmployees() {
      try {
        const response = await axios.get('http://localhost:8080/employees')
        this.employees = response.data.data || response.data || []
        console.log('Employees loaded:', this.employees)
      } catch (error) {
        console.error('Error fetching employees:', error)
      }
    },
    async fetchTasks() {
      try {
        const response = await axios.get('http://localhost:8080/taskMasters')
        this.tasks = response.data.data || []
      } catch (error) {
        console.error('Error fetching tasks:', error)
        alert('Failed to load tasks')
      }
    },
    async fetchMyTimesheets() {
      this.loading = true
      try {
        const response = await axios.get('http://localhost:8080/timesheets/my')
        this.myTimesheets = response.data.data || []
      } catch (error) {
        console.error('Error fetching timesheets:', error)
      } finally {
        this.loading = false
      }
    },
    calculateHours() {
      if (this.formData.startTime && this.formData.endTime) {
        const start = this.formData.startTime.split(':')
        const end = this.formData.endTime.split(':')
        const startMinutes = parseInt(start[0]) * 60 + parseInt(start[1])
        const endMinutes = parseInt(end[0]) * 60 + parseInt(end[1])
        let diffMinutes = endMinutes - startMinutes
        if (diffMinutes < 0) diffMinutes += 24 * 60
        this.calculatedHours = (diffMinutes / 60).toFixed(1)
      }
    },
    async submitTimesheet() {
      if (!this.selectedEmployeeId) {
        alert('Please select an employee')
        return
      }
      if (!this.formData.taskId) {
        alert('Please select a task')
        return
      }
      if (!this.formData.description) {
        alert('Please enter description of work done')
        return
      }
      
      this.submitting = true
      try {
        const payload = {
  employeeId: parseInt(this.selectedEmployeeId),  // Make sure this is sent
  taskId: parseInt(this.formData.taskId),
  taskDate: this.formData.taskDate,
  startTime: this.formData.startTime,
  endTime: this.formData.endTime,
  description: this.formData.description
}
        
        console.log('Submitting:', payload)
        
        const response = await axios.post('http://localhost:8080/timesheets', payload)
        console.log('Response:', response.data)
        
        alert('Timesheet submitted successfully!')
        this.resetForm()
        this.fetchMyTimesheets()
      } catch (error) {
        console.error('Error submitting:', error)
        alert('Failed to submit: ' + (error.response?.data?.message || error.message))
      } finally {
        this.submitting = false
      }
    },
    resetForm() {
      this.selectedEmployeeId = ''
      this.formData = {
        taskId: '',
        taskDate: new Date().toISOString().split('T')[0],
        startTime: '09:00',
        endTime: '17:00',
        description: ''
      }
      this.calculatedHours = 0
    },
    viewDetails(timesheet) {
      this.selectedTimesheet = timesheet
      this.showDetailsModal = true
    },
    closeModal() {
      this.showDetailsModal = false
      this.selectedTimesheet = null
    }
  }
}
</script>

<style scoped>
/* Your existing styles remain the same */
.timesheet-entry {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.header {
  text-align: center;
  margin-bottom: 30px;
}

.header h2 {
  color: #1a237e;
  font-size: 28px;
}

.header p {
  color: #666;
}

.timesheet-form {
  background: white;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  margin-bottom: 40px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 600;
  color: #333;
}

.form-group select,
.form-group input,
.form-group textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
}

.form-group textarea {
  resize: vertical;
  font-family: inherit;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 1fr;
  gap: 15px;
}

.hours-display .hours-badge {
  background: #e8eaf6;
  padding: 10px;
  border-radius: 6px;
  text-align: center;
  font-weight: bold;
  color: #1a237e;
  font-size: 18px;
}

.char-count {
  display: block;
  text-align: right;
  font-size: 12px;
  color: #999;
  margin-top: 5px;
}

.form-actions {
  display: flex;
  gap: 15px;
  justify-content: flex-end;
  margin-top: 30px;
}

.btn-submit {
  background: #4caf50;
  color: white;
  padding: 12px 30px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 16px;
  font-weight: 600;
}

.btn-submit:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.btn-reset {
  background: #999;
  color: white;
  padding: 12px 30px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 16px;
}

.recent-timesheets {
  background: white;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.recent-timesheets h3 {
  color: #1a237e;
  margin-bottom: 20px;
}

.timesheet-table {
  width: 100%;
  border-collapse: collapse;
}

.timesheet-table th,
.timesheet-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.timesheet-table th {
  background: #f5f5f5;
  font-weight: 600;
  color: #333;
}

.status-PENDING {
  color: orange;
  font-weight: bold;
}

.status-APPROVED {
  color: green;
  font-weight: bold;
}

.status-REJECTED {
  color: red;
  font-weight: bold;
}

.btn-view {
  background: #2196f3;
  color: white;
  padding: 5px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

.loading, .no-data {
  text-align: center;
  padding: 40px;
  color: #666;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 30px;
  border-radius: 12px;
  max-width: 600px;
  width: 90%;
  max-height: 80vh;
  overflow-y: auto;
}

.details-modal {
  max-width: 500px;
}

.details-grid {
  display: grid;
  grid-template-columns: 1fr 2fr;
  gap: 15px;
  margin: 20px 0;
}

.detail-item {
  display: contents;
}

.detail-item label {
  font-weight: 600;
  color: #666;
}

.detail-item.full-width {
  display: block;
  grid-column: 1 / -1;
}

.detail-item.full-width label {
  display: block;
  margin-bottom: 5px;
}

.detail-item.full-width p {
  background: #f5f5f5;
  padding: 10px;
  border-radius: 6px;
  margin: 0;
}

.btn-close {
  background: #1a237e;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  width: 100%;
  margin-top: 20px;
}
</style>