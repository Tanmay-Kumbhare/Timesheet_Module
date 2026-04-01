import { createRouter, createWebHistory } from 'vue-router'
import TimesheetEntry from '../views/TimesheetEntry.vue'
import TimesheetModule from '../views/TimesheetModule.vue'
import TimesheetMaster from '../views/master/TimesheetMaster.vue'
import TaskTypeManagement from '../views/master/TaskTypeManagement.vue'
import TaskMasterManagement from '../views/master/TaskMasterManagement.vue'
import NotificationSettings from '../views/master/NotificationSettings.vue'
import TimesheetReports from '../views/reports/TimesheetReports.vue'
import DepartmentwiseReport from '../views/reports/DepartmentwiseReport.vue'
import InstitutewiseReport from '../views/reports/InstitutewiseReport.vue'
import NotificationLogs from '../views/reports/NotificationLogs.vue'
import Home from '../views/Home.vue'
import HodDashboard from '../views/hod/HodDashboard.vue'

const routes = [
  {
    path: '/',
    redirect: '/timesheet/entry'  // Redirect root to timesheet entry
  },
  {
    path: '/timesheet',
    name: 'TimesheetModule',
    component: TimesheetModule
  },
  {
    path: '/timesheet/entry',
    name: 'TimesheetEntry',
    component: TimesheetEntry
  },
  {
    path: '/timesheet/master',
    name: 'TimesheetMaster',
    component: TimesheetMaster
  },
  {
    path: '/timesheet/master/tasktype',
    name: 'TaskTypeManagement',
    component: TaskTypeManagement
  },
  {
    path: '/timesheet/master/taskmaster',
    name: 'TaskMasterManagement',
    component: TaskMasterManagement
  },
  {
    path: '/timesheet/master/notifications',
    name: 'NotificationSettings',
    component: NotificationSettings
  },
  {
    path: '/timesheet/reports',
    name: 'TimesheetReports',
    component: TimesheetReports
  },
  {
    path: '/timesheet/reports/departmentwise',
    name: 'DepartmentwiseReport',
    component: DepartmentwiseReport
  },
  {
    path: '/timesheet/reports/institutewise',
    name: 'InstitutewiseReport',
    component: InstitutewiseReport
  },
  {
    path: '/timesheet/reports/notificationlogs',
    name: 'NotificationLogs',
    component: NotificationLogs
  },
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
  path: '/hod/dashboard',
  name: 'HodDashboard',
  component: HodDashboard
}
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router