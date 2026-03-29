package com.vit.timesheet

import grails.rest.RestfulController

class DepartmentController extends RestfulController<Department> {
    static responseFormats = ['json']
    DepartmentController() { super(Department) }

    @Override
    def index() {
        def list = params.institutionId
            ? Department.findAllByInstitutionAndIsActive(Institution.load(params.institutionId.toLong()), true, [sort: 'name'])
            : Department.findAllByIsActive(true, [sort: 'name'])
        respond list
    }
}