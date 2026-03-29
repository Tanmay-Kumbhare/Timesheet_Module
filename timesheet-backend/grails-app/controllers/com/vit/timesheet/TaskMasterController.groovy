package com.vit.timesheet

import grails.rest.RestfulController

class TaskMasterController extends RestfulController<TaskMaster> {
    static responseFormats = ['json']

    def taskMasterService

    TaskMasterController() {
        super(TaskMaster)
    }

    @Override
def index() {
    def list = TaskMaster.findAllByIsActive(true, [sort: 'name', order: 'asc'])

    def responseList = list.collect { task ->
        [
            id       : task.id,
            name     : task.name,
            taskType : [
                id  : task.taskType?.id,
                name: task.taskType?.name
            ]
        ]
    }

    respond([
        success: true,
        data   : responseList
    ])
}
    @Override
    def save() {
        def result = taskMasterService.saveTask(params)

        if (!result.success) {
            respond result, status: 400
            return
        }

        respond result, status: 201
    }

    def update(Long id) {
        def result = taskMasterService.updateTask(id, params)

        if (!result.success) {
            respond result, status: 400
            return
        }

        respond result
    }

    @Override
    def delete() {
        def result = taskMasterService.deleteTask(params.long('id'))

        if (!result.success) {
            respond result, status: 400
            return
        }

        render status: 204
    }
}