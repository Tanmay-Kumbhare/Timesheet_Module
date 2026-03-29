package com.vit.timesheet

import grails.rest.RestfulController
import grails.converters.JSON

class TaskTypeController extends RestfulController<TaskType> {
    static responseFormats = ['json']

    TaskTypeController() {
        super(TaskType)
    }

    // GET /api/taskTypes
    @Override
    def index() {
        def list = TaskType.findAllByIsActive(true, [sort: 'name', order: 'asc'])
        respond list
    }

    // POST /api/taskTypes
    @Override
    def save() {
        def instance = new TaskType(request.JSON)
        if (!instance.save(flush: true)) {
            respond instance.errors, status: 422
            return
        }
        respond instance, status: 201
    }

    // PUT /api/taskTypes/:id
    @Override
    def update() {
        def instance = TaskType.get(params.id)
        if (!instance) { render status: 404; return }
        instance.properties = request.JSON
        if (!instance.save(flush: true)) {
            respond instance.errors, status: 422
            return
        }
        respond instance
    }

    // DELETE /api/taskTypes/:id
    @Override
    def delete() {
        def instance = TaskType.get(params.id)
        if (!instance) { render status: 404; return }
        instance.isActive = false
        instance.save(flush: true)
        render status: 204
    }
}
