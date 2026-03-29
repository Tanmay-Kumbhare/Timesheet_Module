package com.vit.timesheet

import grails.converters.JSON

class MasterIndexController {
    static responseFormats = ['json']

    // GET /api/masterIndex
    // Returns the master table list shown in wireframe image 3
    def index() {
        def result = [
            [
                srNo      : 1,
                module    : 'Timesheet',
                tableName : 'Task Type',
                records   : TaskType.countByIsActive(true),
                route     : '/master/task-types',
                dbRef     : 'ERPTaskMasterType'
            ],
            [
                srNo      : 2,
                module    : 'Timesheet',
                tableName : 'Task Master',
                records   : TaskMaster.countByIsActive(true),
                route     : '/master/task-master',
                dbRef     : 'Task Master'
            ],
            [
                srNo      : 3,
                module    : 'Timesheet',
                tableName : 'Notification Settings',
                records   : NotificationSettings.countByIsActive(true),
                route     : '/master/notification-settings',
                dbRef     : 'NotificationSettings'
            ]
        ]
        render result as JSON
    }
}