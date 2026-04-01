package com.vit.timesheet

import grails.gorm.transactions.Transactional

@Transactional
class TaskTypeService {
    
    def saveTaskType(params) {
        def instance = new TaskType(
            name: params.name,
            isActive: params.isActive != null ? params.isActive : true
        )
        
        if (!instance.validate()) {
            return [success: false, message: instance.errors.allErrors.collect { it.defaultMessage }]
        }
        
        instance.save(flush: true)
        
        return [
            success: true,
            message: "Task type created",
            data: [
                id       : instance.id,
                name     : instance.name,
                isActive : instance.isActive
            ]
        ]
    }
    
    def updateTaskType(Long id, params) {
        def instance = TaskType.get(id)
        
        if (!instance) {
            return [success: false, message: "Task type not found"]
        }
        
        instance.name = params.name ?: instance.name
        if (params.isActive != null) {
            instance.isActive = params.isActive
        }
        
        if (!instance.validate()) {
            return [success: false, message: instance.errors.allErrors.collect { it.defaultMessage }]
        }
        
        instance.save(flush: true)
        
        return [
            success: true,
            message: "Task type updated",
            data: [
                id       : instance.id,
                name     : instance.name,
                isActive : instance.isActive
            ]
        ]
    }
    
    def deleteTaskType(Long id) {
        def instance = TaskType.get(id)
        
        if (!instance) {
            return [success: false, message: "Task type not found"]
        }
        
        // Check if has associated tasks
        if (instance.tasks && instance.tasks.size() > 0) {
            // Soft delete - just deactivate
            instance.isActive = false
            instance.save(flush: true)
            return [success: true, message: "Task type deactivated (has existing tasks)"]
        }
        
        // Hard delete if no tasks
        instance.delete(flush: true)
        return [success: true, message: "Task type deleted"]
    }
}