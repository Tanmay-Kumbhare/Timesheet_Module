package com.vit.timesheet

import grails.gorm.transactions.Transactional

@Transactional
class TaskMasterService {

    def saveTask(params) {

        def taskType = TaskType.get(params.long('taskTypeId'))

        if (!taskType) {
            return [success: false, message: "Invalid taskTypeId"]
        }

        def instance = new TaskMaster(
                name: params.name,
                taskType: taskType,
                isActive: true
        )

        if (!instance.validate()) {
            return [success: false, message: instance.errors.allErrors.collect { it.defaultMessage }]
        }

        instance.save(flush: true)

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
    }

    def updateTask(Long id, params) {

        def instance = TaskMaster.get(id)

        if (!instance) {
            return [success: false, message: "Task not found"]
        }

        instance.name = params.name ?: instance.name

        if (!instance.validate()) {
            return [success: false, message: instance.errors.allErrors.collect { it.defaultMessage }]
        }

        instance.save(flush: true)

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
    }

    def deleteTask(Long id) {

        def instance = TaskMaster.get(id)

        if (!instance) {
            return [success: false, message: "Task not found"]
        }

        instance.isActive = false
        instance.save(flush: true)

        return [success: true, message: "Task deleted"]
    }
}