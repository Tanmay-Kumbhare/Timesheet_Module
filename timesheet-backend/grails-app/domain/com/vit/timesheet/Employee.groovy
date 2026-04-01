package com.vit.timesheet

class Employee {
    String name
    String employeeNo
    Department department
    String email
    String role
    Boolean isActive = true
    
    static belongsTo = [department: Department]
    
    static constraints = {
        name blank: false, maxSize: 100
        employeeNo blank: false, unique: true, maxSize: 50
        department nullable: false
        email nullable: true, email: true, maxSize: 100
        role inList: ['TEACHER', 'HOD', 'ADMIN', 'COORDINATOR'], nullable: false
        isActive nullable: false
    }
    
    static mapping = {
        table 'employee'
        version false
        id generator: 'identity'
    }
}