package com.vit.timesheet

import grails.rest.RestfulController
import grails.converters.JSON

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
                id: task.id,
                name: task.name,
                taskType: [
                    id: task.taskType?.id,
                    name: task.taskType?.name
                ]
            ]
        }

        respond([
            success: true,
            data: responseList
        ])
    }
    
    @Override
    def save() {
        try {
            println "=== SAVE TASK ==="
            println "Request JSON: ${request.JSON}"
            
            // Extract parameters from JSON
            def jsonBody = request.JSON
            def taskName = jsonBody.name
            def taskTypeId = jsonBody.taskTypeId ?: jsonBody.taskType?.id
            
            println "Task Name: ${taskName}"
            println "Task Type ID: ${taskTypeId}"
            
            if (!taskName || !taskTypeId) {
                respond([success: false, message: "Task name and task type are required"], status: 400)
                return
            }
            
            def result = taskMasterService.saveTask([
                name: taskName,
                taskTypeId: taskTypeId as Long
            ])
            
            println "Service result: ${result}"
            
            if (!result.success) {
                respond result, status: 400
                return
            }
            
            respond result, status: 201
        } catch (Exception e) {
            println "Error in save: ${e.message}"
            e.printStackTrace()
            respond([success: false, message: "Server error: ${e.message}"], status: 500)
        }
    }

    def update(Long id) {
        try {
            println "=== UPDATE TASK ${id} ==="
            println "Request JSON: ${request.JSON}"
            
            def result = taskMasterService.updateTask(id, request.JSON)
            
            if (!result.success) {
                respond result, status: 400
                return
            }
            
            respond result
        } catch (Exception e) {
            println "Error in update: ${e.message}"
            e.printStackTrace()
            respond([success: false, message: "Server error: ${e.message}"], status: 500)
        }
    }

    @Override
    def delete() {
        try {
            def result = taskMasterService.deleteTask(params.long('id'))
            
            if (!result.success) {
                respond result, status: 400
                return
            }
            
            render status: 204
        } catch (Exception e) {
            println "Error in delete: ${e.message}"
            e.printStackTrace()
            respond([success: false, message: "Server error: ${e.message}"], status: 500)
        }
    }
}