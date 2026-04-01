package com.vit.timesheet

import grails.converters.JSON

class EmployeeController {
    
    def index() {
        def employees = Employee.findAllByIsActive(true)
        def result = employees.collect { emp ->
            [
                id: emp.id,
                name: emp.name,
                employeeNo: emp.employeeNo,
                department: [
                    id: emp.department?.id,
                    name: emp.department?.name
                ],
                role: emp.role
            ]
        }
        render([success: true, data: result] as JSON)
    }
}