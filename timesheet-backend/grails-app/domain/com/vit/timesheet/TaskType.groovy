package com.vit.timesheet

class TaskType {
    String  name
    Boolean isActive = true

    static hasMany = [tasks: TaskMaster]

    static constraints = {
        name     blank: false, maxSize: 255
        isActive nullable: false
    }

    static mapping = {
        table 'task_type'
    }
}