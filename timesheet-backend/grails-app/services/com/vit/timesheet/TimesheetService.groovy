package com.vit.timesheet

import grails.gorm.transactions.Transactional
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

@Transactional
class TimesheetService {
    
    def saveTimesheet(Map params, Long employeeId) {
        try {
            println "=== Service: Save Timesheet ==="
            println "Employee ID: ${employeeId}"
            println "Params: ${params}"
            
            def employee = Employee.get(employeeId)
            if (!employee) {
                return [success: false, message: "Employee not found with ID: ${employeeId}"]
            }
            
            def task = TaskMaster.get(params.taskId as Long)
            if (!task) {
                return [success: false, message: "Task not found"]
            }
            
            def taskDate = LocalDate.parse(params.taskDate)
            def startTime = LocalTime.parse(params.startTime)
            def endTime = LocalTime.parse(params.endTime)
            
            def duration = Duration.between(startTime, endTime)
            def totalHours = (duration.toMinutes() / 60.0) as Double
            
            if (totalHours <= 0) {
                return [success: false, message: "End time must be after start time"]
            }
            
            def timesheet = new Timesheet(
                employee: employee,
                task: task,
                taskDate: taskDate,
                startTime: startTime,
                endTime: endTime,
                description: params.description,
                status: 'PENDING',
                totalHours: totalHours,
                submittedDate: new Date(),
                isActive: true
            )
            
            if (!timesheet.validate()) {
                return [success: false, message: timesheet.errors.allErrors.collect { it.defaultMessage }]
            }
            
            timesheet.save(flush: true)
            
            return [
                success: true,
                message: "Timesheet submitted successfully",
                data: formatTimesheet(timesheet)
            ]
            
        } catch (Exception e) {
            println "Error saving timesheet: ${e.message}"
            e.printStackTrace()
            return [success: false, message: "Error: ${e.message}"]
        }
    }
    
    def getEmployeeTimesheets(Long employeeId, Map params = [:]) {
        try {
            def timesheets = Timesheet.findAllByEmployeeId(employeeId, [
                sort: 'taskDate',
                order: 'desc'
            ])
            
            return [
                success: true,
                data: timesheets.collect { formatTimesheet(it) }
            ]
        } catch (Exception e) {
            return [success: false, message: e.message, data: []]
        }
    }
    
    def getDepartmentTimesheets(Long departmentId, String status = 'PENDING') {
        try {
            def employees = Employee.findAllByDepartmentId(departmentId)
            def timesheets = Timesheet.findAllByEmployeeInListAndStatus(employees, status, [
                sort: 'taskDate', 
                order: 'desc'
            ])
            
            return [
                success: true,
                data: timesheets.collect { formatTimesheet(it) }
            ]
        } catch (Exception e) {
            return [success: false, message: e.message, data: []]
        }
    }
    
    def approveTimesheet(Long id, String status, String remarks) {
        try {
            println "=== Service: Approve Timesheet ==="
            println "ID: ${id}, Status: ${status}, Remarks: ${remarks}"
            
            def timesheet = Timesheet.get(id)
            if (!timesheet) {
                return [success: false, message: "Timesheet not found"]
            }
            
            timesheet.status = status
            timesheet.remarks = remarks ?: (status == 'APPROVED' ? 'Approved by HOD' : 'Rejected by HOD')
            timesheet.approvedDate = new Date()
            
            timesheet.save(flush: true)
            
            println "Timesheet ${status} successfully"
            
            return [
                success: true,
                message: "Timesheet ${status.toLowerCase()} successfully",
                data: formatTimesheet(timesheet)
            ]
        } catch (Exception e) {
            println "Error in approveTimesheet: ${e.message}"
            e.printStackTrace()
            return [success: false, message: "Error: ${e.message}"]
        }
    }
    
    def deleteTimesheet(Long id) {
        try {
            def timesheet = Timesheet.get(id)
            if (!timesheet) {
                return [success: false, message: "Timesheet not found"]
            }
            
            timesheet.isActive = false
            timesheet.save(flush: true)
            
            return [success: true, message: "Timesheet deleted"]
        } catch (Exception e) {
            return [success: false, message: e.message]
        }
    }
    
    private def formatTimesheet(Timesheet ts) {
    return [
        id: ts.id,
        employeeId: ts.employee?.id,
        employeeName: ts.employee?.name,
        department: ts.employee?.department?.name,
        taskId: ts.task?.id,
        taskName: ts.task?.name,
        taskType: ts.task?.taskType?.name,
        taskDate: ts.taskDate?.toString(),
        startTime: ts.startTime?.toString(),
        endTime: ts.endTime?.toString(),
        totalHours: ts.totalHours,
        description: ts.description,
        status: ts.status,
        remarks: ts.remarks,
        // Fix: Format dates properly
        submittedDate: ts.submittedDate ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(ts.submittedDate) : null,
        approvedDate: ts.approvedDate ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(ts.approvedDate) : null
    ]
}
}