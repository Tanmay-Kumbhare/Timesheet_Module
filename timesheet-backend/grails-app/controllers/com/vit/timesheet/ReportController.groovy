package com.vit.timesheet

import grails.converters.JSON

class ReportController {
    static responseFormats = ['json']
    ReportService reportService

    /**
     * GET /api/reports/departmentwise
     * Params: departmentId, employeeId, dateFrom, dateTo
     * Mirrors: timesheetReportFilterForHOD + dailytimesheetreportforHOD
     */
    def departmentwise() {
        def filters = [
            departmentId : params.departmentId,
            employeeId   : params.employeeId,
            dateFrom     : params.dateFrom,
            dateTo       : params.dateTo
        ]
        def data = reportService.getDepartmentwiseReport(filters)
        render data as JSON
    }

    /**
     * GET /api/reports/institutewise
     * Params: institutionId, departmentId, dateFrom, dateTo
     * Mirrors: timesheetReportFilter + dailytimesheetreportnew
     */
    def institutewise() {
        def filters = [
            institutionId : params.institutionId,
            departmentId  : params.departmentId,
            dateFrom      : params.dateFrom,
            dateTo        : params.dateTo
        ]
        def data = reportService.getInstitutewiseReport(filters)
        render data as JSON
    }

    /**
     * GET /api/reports/notificationLogs
     * Params: employeeId, status, dateFrom, dateTo
     */
    def notificationLogs() {
        def filters = [
            employeeId : params.employeeId,
            status     : params.status,
            dateFrom   : params.dateFrom,
            dateTo     : params.dateTo
        ]
        def data = reportService.getNotificationLogs(filters)
        render data as JSON
    }
}