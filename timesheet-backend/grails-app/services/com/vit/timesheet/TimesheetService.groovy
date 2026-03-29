package com.vit.timesheet

import grails.gorm.transactions.Transactional

@Transactional
class TimesheetService {

    /**
     * Called after a timesheet is saved.
     * Logs a notification entry per APPROVAL_ALERT settings.
     */
    void triggerNotification(Timesheet timesheet) {
        def settings = NotificationSettings.findAllByNotificationTypeAndIsActive('APPROVAL_ALERT', true)
        settings.each { setting ->
            def message = setting.messageTemplate
                .replace('{employee_name}', timesheet.employee.name)
                .replace('{date}',          timesheet.dateCreated?.format('dd-MM-yyyy') ?: '')

            def log = new NotificationLog(
                notificationSettings : setting,
                employee             : timesheet.employee,
                subject              : setting.subject,
                message              : message,
                status               : 'SENT',
                sentAt               : new Date()
            )
            log.save(flush: true, failOnError: false)
        }
    }
}