package com.vit.timesheet

import grails.rest.RestfulController

class NotificationLogController extends RestfulController<NotificationLog> {
    static responseFormats = ['json']

    NotificationLogController() {
        super(NotificationLog)
    }

    // GET /api/notificationLogs (read-only listing)
    @Override
    def index() {
        def criteria = NotificationLog.createCriteria()
        def list = criteria.list {
            if (params.employeeId) eq('employee', Employee.load(params.employeeId.toLong()))
            if (params.status)     eq('status', params.status)
            order('sentAt', 'desc')
        }
        respond list
    }
}