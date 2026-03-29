package com.vit.timesheet

class NotificationLog {
    NotificationSettings notificationSettings
    Employee             employee
    String               subject
    String               message
    String               status = 'SENT'   // SENT | FAILED
    Date                 sentAt = new Date()

    static belongsTo = [
        notificationSettings: NotificationSettings,
        employee:             Employee
    ]

    static constraints = {
        notificationSettings nullable: true
        employee             nullable: true
        subject              nullable: true, maxSize: 255
        message              nullable: true
        status               blank: false, inList: ['SENT', 'FAILED']
        sentAt               nullable: false
    }

    static mapping = {
        table 'notification_log'
        message type: 'text'
    }
}
