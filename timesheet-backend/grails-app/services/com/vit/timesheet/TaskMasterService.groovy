package com.vit.timesheet

import grails.gorm.transactions.Transactional
import groovy.util.logging.Slf4j

@Slf4j
@Transactional
class TaskMasterService {

    def saveTask(Map params) {
        try {
            log.info "Saving task with params: ${params}"
            
            def taskTypeId = params.taskTypeId ?: params.taskType?.id
            log.info "Task Type ID: ${taskTypeId}"
            
            def taskType = TaskType.get(taskTypeId as Long)
            
            if (!taskType) {
                log.error "Task type not found with ID: ${taskTypeId}"
                return [success: false, message: "Invalid taskTypeId: ${taskTypeId}"]
            }

            def instance = new TaskMaster(
                name: params.name,
                taskType: taskType,
                isActive: true
            )
            
            log.info "Created instance: ${instance}"

            if (!instance.validate()) {
                log.error "Validation errors: ${instance.errors.allErrors}"
                return [success: false, message: instance.errors.allErrors.collect { it.defaultMessage }]
            }

            instance.save(flush: true, failOnError: true)
            log.info "Task saved successfully with ID: ${instance.id}"

            return [
                success: true,
                message: "Task created",
                data: [
                    id   : instance.id,
                    name : instance.name,
                    taskType: [
                        id  : instance.taskType?.id,
                        name: instance.taskType?.name
                    ]
                ]
            ]
        } catch (Exception e) {
            log.error "Error saving task: ${e.message}", e
            return [success: false, message: "Error saving task: ${e.message}"]
        }
    }

    def updateTask(Long id, Map params) {
        try {
            log.info "Updating task ${id} with params: ${params}"
            
            def instance = TaskMaster.get(id)

            if (!instance) {
                return [success: false, message: "Task not found"]
            }

            if (params.name) {
                instance.name = params.name
            }
            
            // Handle taskType update
            def taskTypeId = params.taskTypeId ?: params.taskType?.id
            if (taskTypeId) {
                def taskType = TaskType.get(taskTypeId as Long)
                if (taskType) {
                    instance.taskType = taskType
                }
            }

            if (!instance.validate()) {
                log.error "Validation errors: ${instance.errors.allErrors}"
                return [success: false, message: instance.errors.allErrors.collect { it.defaultMessage }]
            }

            instance.save(flush: true, failOnError: true)
            log.info "Task updated successfully"

            return [
                success: true,
                message: "Task updated",
                data: [
                    id   : instance.id,
                    name : instance.name,
                    taskType: [
                        id  : instance.taskType?.id,
                        name: instance.taskType?.name
                    ]
                ]
            ]
        } catch (Exception e) {
            log.error "Error updating task: ${e.message}", e
            return [success: false, message: "Error updating task: ${e.message}"]
        }
    }

    def deleteTask(Long id) {
        try {
            log.info "Deleting task ${id}"
            
            def instance = TaskMaster.get(id)

            if (!instance) {
                return [success: false, message: "Task not found"]
            }

            instance.isActive = false
            instance.save(flush: true, failOnError: true)

            return [success: true, message: "Task deleted"]
        } catch (Exception e) {
            log.error "Error deleting task: ${e.message}", e
            return [success: false, message: "Error deleting task: ${e.message}"]
        }
    }
}