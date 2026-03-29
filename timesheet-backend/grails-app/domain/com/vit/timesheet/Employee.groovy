package com.vit.timesheet

class Employee {
    String     empNo
    String     name
    String     email
    Department department
    String     role   = 'EMPLOYEE'   // EMPLOYEE | COORDINATOR | HOD
    Boolean    isActive = true

    static belongsTo = [department: Department]

    static constraints = {
        empNo      blank: false, maxSize: 50, unique: true
        name       blank: false, maxSize: 255
        email      nullable: true,  email: true
        department nullable: false
        role       blank: false, inList: ['EMPLOYEE', 'COORDINATOR', 'HOD']
        isActive   nullable: false
    }

    static mapping = {
        table 'employee'
    }
}