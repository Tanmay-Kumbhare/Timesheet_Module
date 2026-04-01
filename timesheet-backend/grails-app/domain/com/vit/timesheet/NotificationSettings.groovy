package com.vit.timesheet

class NotificationSettings {
    String notificationType  // EMAIL_REMINDER, APPROVAL_ALERT, etc.
    String recipientRole     // EMPLOYEE, HOD, ADMIN
    String subject
    String messageTemplate
    Boolean isActive = true
    
    static constraints = {
        notificationType blank: false, maxSize: 50, inList: ['EMAIL_REMINDER', 'APPROVAL_ALERT', 'TIMESHEET_SUBMISSION', 'DEADLINE_REMINDER']
        recipientRole blank: false, maxSize: 20, inList: ['EMPLOYEE', 'HOD', 'ADMIN', 'COORDINATOR']
        subject blank: false, maxSize: 200
        messageTemplate blank: false, maxSize: 500
        isActive nullable: false
    }
    
    static mapping = {
        table 'notification_settings'
        version false
    }
}