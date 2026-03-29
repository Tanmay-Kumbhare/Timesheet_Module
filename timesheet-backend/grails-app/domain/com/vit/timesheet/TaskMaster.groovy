package com.vit.timesheet

class TaskMaster {
    String   name
    TaskType taskType
    Boolean  isActive = true

    static belongsTo = [taskType: TaskType]

    static constraints = {
    name blank: false, maxSize: 255, validator: { val, obj ->

        def existingTask = TaskMaster.findByNameIlikeAndIsActive(val, true)

        if (existingTask && existingTask.id != obj.id) {
            return ["Task name already exists"]
        }
    }

    taskType nullable: false
    isActive nullable: false
}

    static mapping = {
        table 'task_master'
    }
}
