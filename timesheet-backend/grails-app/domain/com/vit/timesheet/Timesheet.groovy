package com.vit.timesheet

import java.time.LocalDate
import java.time.LocalTime

class Timesheet {
    Employee employee
    TaskMaster task
    LocalDate taskDate
    LocalTime startTime
    LocalTime endTime
    String description
    String status
    String remarks
    Double totalHours
    Date submittedDate
    Date approvedDate
    Boolean isActive = true
    
    static belongsTo = [employee: Employee, task: TaskMaster]
    
    static constraints = {
        employee nullable: false
        task nullable: false
        taskDate nullable: false
        startTime nullable: false
        endTime nullable: false
        description nullable: false, maxSize: 1000
        status inList: ['PENDING', 'APPROVED', 'REJECTED']
        remarks nullable: true, maxSize: 500
        totalHours nullable: true
        submittedDate nullable: true
        approvedDate nullable: true
        isActive nullable: false
    }
    
    static mapping = {
        table 'timesheet'
        version false
        id generator: 'identity'
    }
}