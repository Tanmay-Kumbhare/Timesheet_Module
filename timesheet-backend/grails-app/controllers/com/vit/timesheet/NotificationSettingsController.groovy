package com.vit.timesheet

import grails.rest.RestfulController

class NotificationSettingsController extends RestfulController<NotificationSettings> {
    static responseFormats = ['json']

    NotificationSettingsController() {
        super(NotificationSettings)
    }

    @Override
    def index() {
        def list = NotificationSettings.findAllByIsActive(true, [sort: 'notificationType', order: 'asc'])
        respond list
    }

    @Override
    def save() {
        def instance = new NotificationSettings(params)
        if (!instance.save(flush: true)) {
            respond instance.errors, status: 422
            return
        }
        respond instance, status: 201
    }

    @Override
    def update() {
        def instance = NotificationSettings.get(params.id)
        if (!instance) { render status: 404; return }
        instance.properties = params
        if (!instance.save(flush: true)) {
            respond instance.errors, status: 422
            return
        }
        respond instance
    }

    @Override
    def delete() {
        def instance = NotificationSettings.get(params.id)
        if (!instance) { render status: 404; return }
        instance.isActive = false
        instance.save(flush: true)
        render status: 204
    }
}