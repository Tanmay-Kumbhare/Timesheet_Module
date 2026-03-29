import { createRouter, createWebHistory } from "vue-router"

import TimesheetDashboard from "@/views/dashboard/TimesheetDashboard.vue"

import TimesheetMaster from "@/views/master/TimesheetMaster.vue"
import TaskTypeManagement from "@/views/master/TaskTypeManagement.vue"
import TaskMasterManagement from "@/views/master/TaskMasterManagement.vue"
import NotificationSettings from "@/views/master/NotificationSettings.vue"

import TimesheetReports from "@/views/reports/TimesheetReports.vue"
import DepartmentwiseReport from "@/views/reports/DepartmentwiseReport.vue"
import InstitutewiseReport from "@/views/reports/InstitutewiseReport.vue"
import NotificationLogs from "@/views/reports/NotificationLogs.vue"

const routes = [

{
 path:"/",
 component:TimesheetDashboard
},

{
 path:"/timesheet/master",
 component:TimesheetMaster,
 children:[

  {
   path:"task-types",
   component:TaskTypeManagement
  },

  {
   path:"task-master",
   component:TaskMasterManagement
  },

  {
   path:"notifications",
   component:NotificationSettings
  }

 ]
},

{
 path:"/timesheet/reports",
 component:TimesheetReports,
 children:[

 {
  path:"departmentwise",
  component:DepartmentwiseReport
 },

 {
  path:"institutewise",
  component:InstitutewiseReport
 },

 {
  path:"logs",
  component:NotificationLogs
 }

 ]
}

]

export default createRouter({
 history:createWebHistory(),
 routes
})