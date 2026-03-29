package com.vit.timesheet

class Timesheet {
    Employee   employee
    TaskMaster taskMaster
    Date       dateFrom
    Date       dateTo
    BigDecimal hoursSpent
    String     description

    static belongsTo = [
        employee:   Employee,
        taskMaster: TaskMaster
    ]

    static constraints = {
        employee    nullable: false
        taskMaster  nullable: false
        dateFrom    nullable: false
        dateTo      nullable: false
        hoursSpent  nullable: false, min: 0.0G, max: 999.99G
        description nullable: true
    }

    static mapping = {
        table 'timesheet'
        description type: 'text'
    }
}