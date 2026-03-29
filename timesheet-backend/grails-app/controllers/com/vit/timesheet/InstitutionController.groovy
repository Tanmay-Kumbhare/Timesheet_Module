package com.vit.timesheet

import grails.rest.RestfulController

class InstitutionController extends RestfulController<Institution> {
    static responseFormats = ['json']
    InstitutionController() { super(Institution) }

    @Override
    def index() {
        respond Institution.findAllByIsActive(true, [sort: 'name', order: 'asc'])
    }
}