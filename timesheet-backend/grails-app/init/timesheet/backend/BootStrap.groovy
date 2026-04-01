package com.vit.timesheet

class BootStrap {
    
    def init = { servletContext ->
        println "=== Initializing Bootstrap Data ==="
        
        // Create default department with explicit save
        def dept = Department.findByName("Computer Engineering")
        if (!dept) {
            dept = new Department(
                name: "Computer Engineering",
                code: "COMP",
                isActive: true
            )
            dept.save(flush: true, failOnError: true)
            println "Created department: ${dept.id} - ${dept.name}"
        } else {
            println "Found existing department: ${dept.id} - ${dept.name}"
        }
        
        // Create default employee
        def employee = Employee.findByEmployeeNo("10791")
        if (!employee) {
            employee = new Employee(
                name: "Dipak Pawar",
                employeeNo: "10791",
                department: dept,
                role: "TEACHER",
                isActive: true
            )
            employee.save(flush: true, failOnError: true)
            println "Created employee: ${employee.id} - ${employee.name}"
        } else {
            println "Found existing employee: ${employee.id} - ${employee.name}"
        }
        
        println "=== Bootstrap Complete ==="
    }
    
    def destroy = {
    }
}