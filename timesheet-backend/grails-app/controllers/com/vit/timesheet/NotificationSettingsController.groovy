package com.vit.timesheet

import grails.rest.RestfulController

class NotificationSettingsController extends RestfulController<NotificationSettings> {
    static responseFormats = ['json']
    
    def notificationSettingsService
    
    NotificationSettingsController() {
        super(NotificationSettings)
    }
    
    @Override
    def index() {
        def list = NotificationSettings.findAllByIsActive(true, [sort: 'notificationType', order: 'asc'])
        
        respond([
            success: true,
            data: list.collect { [
                id: it.id,
                notificationType: it.notificationType,
                recipientRole: it.recipientRole,
                subject: it.subject,
                messageTemplate: it.messageTemplate,
                isActive: it.isActive
            ] }
        ])
    }
    
    @Override
    def save() {
        def result = notificationSettingsService.saveNotification(request.JSON)
        
        if (!result.success) {
            respond result, status: 400
            return
        }
        
        respond result, status: 201
    }
    
    def update(Long id) {
        def result = notificationSettingsService.updateNotification(id, request.JSON)
        
        if (!result.success) {
            respond result, status: 400
            return
        }
        
        respond result
    }
    
    @Override
    def delete() {
        def result = notificationSettingsService.deleteNotification(params.long('id'))
        
        if (!result.success) {
            respond result, status: 400
            return
        }
        
        render status: 204
    }
}