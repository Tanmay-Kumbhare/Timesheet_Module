package com.vit.timesheet

import grails.gorm.transactions.Transactional

@Transactional
class NotificationSettingsService {
    
    def saveNotification(params) {
        def instance = new NotificationSettings(
            notificationType: params.notificationType,
            recipientRole: params.recipientRole,
            subject: params.subject,
            messageTemplate: params.messageTemplate,
            isActive: params.isActive != null ? params.isActive : true
        )
        
        if (!instance.validate()) {
            return [success: false, message: instance.errors.allErrors.collect { it.defaultMessage }]
        }
        
        instance.save(flush: true)
        
        return [
            success: true,
            message: "Notification setting created",
            data: instance
        ]
    }
    
    def updateNotification(Long id, params) {
        def instance = NotificationSettings.get(id)
        
        if (!instance) {
            return [success: false, message: "Notification setting not found"]
        }
        
        instance.notificationType = params.notificationType ?: instance.notificationType
        instance.recipientRole = params.recipientRole ?: instance.recipientRole
        instance.subject = params.subject ?: instance.subject
        instance.messageTemplate = params.messageTemplate ?: instance.messageTemplate
        if (params.isActive != null) {
            instance.isActive = params.isActive
        }
        
        if (!instance.validate()) {
            return [success: false, message: instance.errors.allErrors.collect { it.defaultMessage }]
        }
        
        instance.save(flush: true)
        
        return [
            success: true,
            message: "Notification setting updated",
            data: instance
        ]
    }
    
    def deleteNotification(Long id) {
        def instance = NotificationSettings.get(id)
        
        if (!instance) {
            return [success: false, message: "Notification setting not found"]
        }
        
        // Soft delete
        instance.isActive = false
        instance.save(flush: true)
        
        return [success: true, message: "Notification setting deactivated"]
    }
}