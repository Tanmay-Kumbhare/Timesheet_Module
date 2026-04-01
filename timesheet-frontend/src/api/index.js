import axios from 'axios'

// NO /api prefix - use direct endpoints
const API_BASE_URL = 'http://localhost:8080'

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: { 'Content-Type': 'application/json' }
})

// Add response interceptor to handle Grails responses
api.interceptors.response.use(
  response => {
    console.log('API Response:', response.config.url, response.data)
    // If response has data property, extract it
    if (response.data && response.data.data !== undefined) {
      return response.data.data
    }
    return response.data
  },
  error => {
    console.error('API Error:', error.response?.status, error.response?.data)
    return Promise.reject(error)
  }
)

// Task Master APIs
export const taskMasterApi = {
  getAll: () => api.get('/taskMasters'),
  getById: (id) => api.get(`/taskMasters/${id}`),
  create: (data) => api.post('/taskMasters', data),
  update: (id, data) => api.put(`/taskMasters/${id}`, data),
  delete: (id) => api.delete(`/taskMasters/${id}`)
}

// Task Type APIs
export const taskTypeApi = {
  getAll: () => api.get('/taskTypes'),
  getById: (id) => api.get(`/taskTypes/${id}`),
  create: (data) => api.post('/taskTypes', data),
  update: (id, data) => api.put(`/taskTypes/${id}`, data),
  delete: (id) => api.delete(`/taskTypes/${id}`)
}

// Notification Settings APIs
export const notificationApi = {
  getAll: () => api.get('/notificationSettings'),
  getById: (id) => api.get(`/notificationSettings/${id}`),
  create: (data) => api.post('/notificationSettings', data),
  update: (id, data) => api.put(`/notificationSettings/${id}`, data),
  delete: (id) => api.delete(`/notificationSettings/${id}`)
}

// Timesheet APIs
export const timesheetApi = {
  create: (data) => api.post('/timesheets', data),
  getMyTimesheets: () => api.get('/timesheets/my'),
  getAll: () => api.get('/timesheets')
}

// Report APIs
export const reportApi = {
  getDepartmentwise: (params) => api.get('/report/departmentwise', { params }),
  getInstitutewise: (params) => api.get('/report/institutewise', { params }),
  getNotificationLogs: (params) => api.get('/report/notificationLogs', { params })
}

export default api