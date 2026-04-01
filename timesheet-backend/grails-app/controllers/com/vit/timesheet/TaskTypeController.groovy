package com.vit.timesheet

import grails.rest.RestfulController

class TaskTypeController extends RestfulController<TaskType> {
    static responseFormats = ['json']
    
    def taskTypeService
    
    TaskTypeController() {
        super(TaskType)
    }
    
    @Override
    def index() {
        def list = TaskType.findAllByIsActive(true, [sort: 'name', order: 'asc'])
        
        def responseList = list.collect { type ->
            [
                id       : type.id,
                name     : type.name,
                isActive : type.isActive,
                tasks    : type.tasks?.collect { [id: it.id] } ?: []
            ]
        }
        
        respond([
            success: true,
            data   : responseList
        ])
    }
    
    @Override
    def save() {
        def result = taskTypeService.saveTaskType(request.JSON)
        
        if (!result.success) {
            respond result, status: 400
            return
        }
        
        respond result, status: 201
    }
    
    def update(Long id) {
        def result = taskTypeService.updateTaskType(id, request.JSON)
        
        if (!result.success) {
            respond result, status: 400
            return
        }
        
        respond result
    }
    
    @Override
    def delete() {
        def result = taskTypeService.deleteTaskType(params.long('id'))
        
        if (!result.success) {
            respond result, status: 400
            return
        }
        
        render status: 204
    }
}