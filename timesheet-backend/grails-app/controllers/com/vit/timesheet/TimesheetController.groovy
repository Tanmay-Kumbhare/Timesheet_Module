package com.vit.timesheet

import grails.rest.RestfulController
import grails.converters.JSON

class TimesheetController extends RestfulController<Timesheet> {
    static responseFormats = ['json']
    TimesheetService timesheetService

    TimesheetController() {
        super(Timesheet)
    }

    // GET /api/timesheets?employeeId=&taskMasterId=&dateFrom=&dateTo=
    @Override
    def index() {
        def criteria = Timesheet.createCriteria()
        def list = criteria.list {
            if (params.employeeId)   eq('employee',   Employee.load(params.employeeId.toLong()))
            if (params.taskMasterId) eq('taskMaster', TaskMaster.load(params.taskMasterId.toLong()))
            if (params.dateFrom)     gte('dateFrom',  Date.parse('yyyy-MM-dd', params.dateFrom))
            if (params.dateTo)       lte('dateTo',    Date.parse('yyyy-MM-dd', params.dateTo))
            order('dateCreated', 'desc')
        }
        respond list
    }

    // POST /api/timesheets
    @Override
    def save() {
        def employee   = Employee.get(params.employeeId)
        def taskMaster = TaskMaster.get(params.taskMasterId)
        if (!employee || !taskMaster) {
            render status: 400, text: 'Invalid employeeId or taskMasterId'
            return
        }
        def instance = new Timesheet(
            employee:    employee,
            taskMaster:  taskMaster,
            dateFrom:    Date.parse('yyyy-MM-dd', params.dateFrom),
            dateTo:      Date.parse('yyyy-MM-dd', params.dateTo),
            hoursSpent:  params.hoursSpent as BigDecimal,
            description: params.description
        )
        if (!instance.save(flush: true)) {
            respond instance.errors, status: 422
            return
        }
        timesheetService.triggerNotification(instance)
        respond instance, status: 201
    }

    // PUT /api/timesheets/:id
    @Override
    def update() {
        def instance = Timesheet.get(params.id)
        if (!instance) { render status: 404; return }
        if (params.taskMasterId) instance.taskMaster = TaskMaster.get(params.taskMasterId)
        if (params.dateFrom)     instance.dateFrom   = Date.parse('yyyy-MM-dd', params.dateFrom)
        if (params.dateTo)       instance.dateTo     = Date.parse('yyyy-MM-dd', params.dateTo)
        if (params.hoursSpent)   instance.hoursSpent = params.hoursSpent as BigDecimal
        if (params.description)  instance.description = params.description
        if (!instance.save(flush: true)) {
            respond instance.errors, status: 422
            return
        }
        respond instance
    }

    // DELETE /api/timesheets/:id
    @Override
    def delete() {
        def instance = Timesheet.get(params.id)
        if (!instance) { render status: 404; return }
        instance.delete(flush: true)
        render status: 204
    }
}