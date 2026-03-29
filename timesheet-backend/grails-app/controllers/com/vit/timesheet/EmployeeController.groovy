package com.vit.timesheet

import grails.rest.RestfulController

class EmployeeController extends RestfulController<Employee> {
    static responseFormats = ['json']
    EmployeeController() { super(Employee) }

    @Override
    def index() {
        def list = params.departmentId
            ? Employee.findAllByDepartmentAndIsActive(Department.load(params.departmentId.toLong()), true, [sort: 'name'])
            : Employee.findAllByIsActive(true, [sort: 'name'])
        respond list
    }
}