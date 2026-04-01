package com.vit.timesheet

class TaskType {
    String name
    Boolean isActive = true
    Long version = 0  // Add this line for Grails versioning
    
    static hasMany = [tasks: TaskMaster]
    
    static constraints = {
        name blank: false, unique: true, maxSize: 100
        isActive nullable: false
    }
    
    static mapping = {
        table 'task_type'
        version true  // Enable version column
        id generator: 'identity'
    }
}