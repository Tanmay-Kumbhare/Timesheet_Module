// grails-app/controllers/com/vit/timesheet/ReportController.groovy
package com.vit.timesheet

import grails.converters.JSON
import java.time.LocalDate

class ReportController {
    
    def departmentwise() {
        try {
            def departmentId = params.departmentId
            def startDate = params.startDate
            def endDate = params.endDate
            
            def query = Timesheet.where {
                isActive == true
            }
            
            if (departmentId && departmentId != '') {
                query = query.where {
                    employee.department.id == departmentId.toLong()
                }
            }
            
            if (startDate && startDate != '') {
                def start = LocalDate.parse(startDate)
                query = query.where {
                    taskDate >= start
                }
            }
            
            if (endDate && endDate != '') {
                def end = LocalDate.parse(endDate)
                query = query.where {
                    taskDate <= end
                }
            }
            
            def timesheets = query.list()
            
            def reportData = []
            timesheets.each { ts ->
                reportData.add([
                    id: ts.id,
                    employeeName: ts.employee?.name ?: 'N/A',
                    employeeNo: ts.employee?.employeeNo ?: 'N/A',
                    departmentName: ts.employee?.department?.name ?: 'N/A',
                    taskName: ts.task?.name ?: 'N/A',
                    taskType: ts.task?.taskType?.name ?: 'N/A',
                    date: ts.taskDate?.toString(),
                    hours: ts.totalHours,
                    status: ts.status ?: 'PENDING'
                ])
            }
            
            render([success: true, data: reportData] as JSON)
        } catch (Exception e) {
            println "Error in departmentwise report: ${e.message}"
            e.printStackTrace()
            render(status: 500, text: [success: false, message: e.message] as JSON)
        }
    }
    
    def institutewise() {
        try {
            def institutionId = params.institutionId
            def month = params.month
            
            def timesheets = Timesheet.findAllByIsActive(true)
            
            def reportData = []
            timesheets.each { ts ->
                reportData.add([
                    id: ts.id,
                    employeeName: ts.employee?.name ?: 'N/A',
                    employeeNo: ts.employee?.employeeNo ?: 'N/A',
                    departmentName: ts.employee?.department?.name ?: 'N/A',
                    institutionName: "Vishwakarma Institute of Technology, Pune",
                    taskName: ts.task?.name ?: 'N/A',
                    date: ts.taskDate?.toString(),
                    hours: ts.totalHours,
                    status: ts.status ?: 'PENDING'
                ])
            }
            
            render([success: true, data: reportData] as JSON)
        } catch (Exception e) {
            println "Error in institutewise report: ${e.message}"
            e.printStackTrace()
            render(status: 500, text: [success: false, message: e.message] as JSON)
        }
    }
    
    def notificationlogs() {
        try {
            def startDate = params.startDate
            def endDate = params.endDate
            def status = params.status
            
            // Return empty array for now - implement when notification system is ready
            def logs = []
            
            render([success: true, data: logs] as JSON)
        } catch (Exception e) {
            println "Error in notificationlogs: ${e.message}"
            e.printStackTrace()
            render(status: 500, text: [success: false, message: e.message] as JSON)
        }
    }
}