package com.vit.timesheet

import grails.converters.JSON

class DepartmentController {
    
    def index() {
        def departments = Department.findAllByIsActive(true)
        render([success: true, data: departments] as JSON)
    }
}