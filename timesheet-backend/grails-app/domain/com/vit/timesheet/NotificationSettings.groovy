package com.vit.timesheet

class NotificationSettings {
    String  notificationType   // EMAIL_REMINDER | APPROVAL_ALERT
    String  recipientRole      // EMPLOYEE | HOD | COORDINATOR
    String  subject
    String  messageTemplate
    Boolean isActive = true

    static constraints = {
        notificationType blank: false, inList: ['EMAIL_REMINDER', 'APPROVAL_ALERT']
        recipientRole    blank: false, inList: ['EMPLOYEE', 'HOD', 'COORDINATOR']
        subject          blank: false, maxSize: 255
        messageTemplate  blank: false
        isActive         nullable: false
    }

    static mapping = {
        table 'notification_settings'
        messageTemplate type: 'text'
    }
}