package com.vit.timesheet

class Department {
    String name
    String code
    Boolean isActive = true
    
    static hasMany = [employees: Employee]
    
    static constraints = {
        name blank: false, unique: true, maxSize: 100
        code blank: false, unique: true, maxSize: 20
        isActive nullable: false
    }
    
    static mapping = {
        table 'department'
        version false
        id generator: 'identity'
    }
}