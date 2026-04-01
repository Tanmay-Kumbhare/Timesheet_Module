package com.vit.timesheet

class UrlMappings {
    static mappings = {
        // Task Management
        "/taskMasters"(resources: "taskMaster")
        "/taskTypes"(resources: "taskType")
        "/notificationSettings"(resources: "notificationSettings")
        
        // Employee and Department
        "/employees"(controller: "employee", action: "index", method: "GET")
        "/departments"(controller: "department", action: "index", method: "GET")
        
        // Timesheet APIs
        "/timesheets"(controller: "timesheet", action: "save", method: "POST")
        "/timesheets/my"(controller: "timesheet", action: "myTimesheets", method: "GET")
        "/timesheets/all"(controller: "timesheet", action: "allTimesheets", method: "GET")
        "/timesheets/approve"(controller: "timesheet", action: "approve", method: "POST")
        
        "/"(view: "/index")
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}