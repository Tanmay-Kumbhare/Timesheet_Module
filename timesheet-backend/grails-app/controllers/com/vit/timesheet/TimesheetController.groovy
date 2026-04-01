package com.vit.timesheet

import grails.converters.JSON

class TimesheetController {
    
    def timesheetService
    
    // ==================== SAVE TIMESHEET ====================
    def save() {
        println "=== SAVE TIMESHEET ==="
        println "Request JSON: ${request.JSON}"
        
        try {
            def json = request.JSON
            def employeeId = json.employeeId as Long
            
            if (!employeeId) {
                render(status: 400, text: [success: false, message: "Employee ID is required"] as JSON)
                return
            }
            
            def params = [
                taskId: json.taskId,
                taskDate: json.taskDate,
                startTime: json.startTime,
                endTime: json.endTime,
                description: json.description
            ]
            
            def result = timesheetService.saveTimesheet(params, employeeId)
            
            if (!result.success) {
                render(status: 400, text: result as JSON)
                return
            }
            
            render(status: 201, text: result as JSON)
            
        } catch (Exception e) {
            println "Error in save: ${e.message}"
            e.printStackTrace()
            render(status: 500, text: [success: false, message: "Server error: ${e.message}"] as JSON)
        }
    }
    
    // ==================== GET MY TIMESHEETS ====================
    def myTimesheets() {
        println "=== MY TIMESHEETS ==="
        
        try {
            def employeeId = params.employeeId as Long
            
            if (!employeeId) {
                render([success: true, data: []] as JSON)
                return
            }
            
            def result = timesheetService.getEmployeeTimesheets(employeeId)
            render(result as JSON)
            
        } catch (Exception e) {
            println "Error in myTimesheets: ${e.message}"
            render(status: 500, text: [success: false, message: "Server error: ${e.message}"] as JSON)
        }
    }
    
    // ==================== GET ALL TIMESHEETS (HOD & Reports) ====================
    def allTimesheets() {
        println "=== ALL TIMESHEETS (HOD/Reports) ==="
        println "Params: ${params}"
        
        try {
            def departmentId = params.departmentId as Long
            def status = params.status
            def fromDate = params.fromDate
            def toDate = params.toDate
            
            def criteria = Timesheet.createCriteria()
            def timesheets = criteria.list {
                // Department filter
                if (departmentId) {
                    employee {
                        eq('department.id', departmentId)
                    }
                }
                // Status filter
                if (status && status != '') {
                    eq('status', status)
                }
                // Date range filter
                if (fromDate) {
                    ge('taskDate', LocalDate.parse(fromDate))
                }
                if (toDate) {
                    le('taskDate', LocalDate.parse(toDate))
                }
                order('taskDate', 'desc')
            }
            
            def result = timesheets.collect { formatTimesheet(it) }
            render([success: true, data: result] as JSON)
            
        } catch (Exception e) {
            println "Error in allTimesheets: ${e.message}"
            e.printStackTrace()
            render(status: 500, text: [success: false, message: "Server error: ${e.message}"] as JSON)
        }
    }
    
    // ==================== APPROVE/REJECT TIMESHEET ====================
    def approve() {
        println "=== APPROVE TIMESHEET ==="
        println "Params: ${params}"
        
        try {
            def id = params.id as Long
            def status = params.status
            def remarks = params.remarks
            
            println "ID: ${id}, Status: ${status}, Remarks: ${remarks}"
            
            if (!id) {
                render(status: 400, text: [success: false, message: "Timesheet ID is required"] as JSON)
                return
            }
            
            if (!status) {
                render(status: 400, text: [success: false, message: "Status is required"] as JSON)
                return
            }
            
            def result = timesheetService.approveTimesheet(id, status, remarks)
            
            if (!result.success) {
                render(status: 400, text: result as JSON)
                return
            }
            
            render(result as JSON)
            
        } catch (Exception e) {
            println "Error in approve: ${e.message}"
            e.printStackTrace()
            render(status: 500, text: [success: false, message: "Server error: ${e.message}"] as JSON)
        }
    }
    
    // ==================== HELPER METHOD ====================
    // ==================== HELPER METHOD ====================
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