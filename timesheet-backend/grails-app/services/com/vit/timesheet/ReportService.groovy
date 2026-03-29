package com.vit.timesheet

import grails.gorm.transactions.Transactional
import groovy.sql.Sql
import javax.sql.DataSource

@Transactional(readOnly = true)
class ReportService {

    DataSource dataSource

    // ----------------------------------------------------------
    // Departmentwise Report
    // ----------------------------------------------------------
    // Used by: GET /api/reports/departmentwise
    // Mirrors: timesheetReportFilterForHOD / dailytimesheetreportforHOD
    // ----------------------------------------------------------
    List getDepartmentwiseReport(Map filters) {
        def sql = new Sql(dataSource)
        def query = """
            SELECT
                e.id            AS employee_id,
                e.emp_no        AS emp_no,
                e.name          AS employee_name,
                d.id            AS department_id,
                d.name          AS department_name,
                i.name          AS institution_name,
                tm.name         AS task_name,
                tt.name         AS task_type,
                t.date_from,
                t.date_to,
                t.hours_spent,
                t.description,
                t.date_created
            FROM timesheet t
            JOIN employee   e  ON t.employee_id    = e.id
            JOIN department d  ON e.department_id  = d.id
            JOIN institution i ON d.institution_id = i.id
            JOIN task_master tm ON t.task_master_id = tm.id
            JOIN task_type   tt ON tm.task_type_id  = tt.id
            WHERE 1 = 1
        """
        def params = []

        if (filters.departmentId) {
            query += " AND d.id = ? "
            params << filters.departmentId.toLong()
        }
        if (filters.employeeId) {
            query += " AND e.id = ? "
            params << filters.employeeId.toLong()
        }
        if (filters.dateFrom) {
            query += " AND t.date_from >= ? "
            params << filters.dateFrom
        }
        if (filters.dateTo) {
            query += " AND t.date_to <= ? "
            params << filters.dateTo
        }
        query += " ORDER BY d.name, e.name, t.date_from DESC"

        def rows = sql.rows(query, params)
        sql.close()
        return rows.collect { row ->
            [
                employeeId      : row.employee_id,
                empNo           : row.emp_no,
                employeeName    : row.employee_name,
                departmentId    : row.department_id,
                departmentName  : row.department_name,
                institutionName : row.institution_name,
                taskName        : row.task_name,
                taskType        : row.task_type,
                dateFrom        : row.date_from?.toString(),
                dateTo          : row.date_to?.toString(),
                hoursSpent      : row.hours_spent,
                description     : row.description,
                dateCreated     : row.date_created?.toString()
            ]
        }
    }

    // ----------------------------------------------------------
    // Institutewise Report
    // ----------------------------------------------------------
    // Used by: GET /api/reports/institutewise
    // Mirrors: timesheetReportFilter / dailytimesheetreportnew
    // ----------------------------------------------------------
    List getInstitutewiseReport(Map filters) {
        def sql = new Sql(dataSource)
        def query = """
            SELECT
                i.id            AS institution_id,
                i.name          AS institution_name,
                d.id            AS department_id,
                d.name          AS department_name,
                e.id            AS employee_id,
                e.emp_no        AS emp_no,
                e.name          AS employee_name,
                tm.name         AS task_name,
                tt.name         AS task_type,
                t.date_from,
                t.date_to,
                t.hours_spent,
                t.description,
                t.date_created,
                SUM(t.hours_spent) OVER (PARTITION BY d.id, t.date_from)
                                AS dept_daily_hours
            FROM timesheet t
            JOIN employee    e  ON t.employee_id    = e.id
            JOIN department  d  ON e.department_id  = d.id
            JOIN institution i  ON d.institution_id = i.id
            JOIN task_master tm ON t.task_master_id = tm.id
            JOIN task_type   tt ON tm.task_type_id  = tt.id
            WHERE 1 = 1
        """
        def params = []

        if (filters.institutionId) {
            query += " AND i.id = ? "
            params << filters.institutionId.toLong()
        }
        if (filters.departmentId) {
            query += " AND d.id = ? "
            params << filters.departmentId.toLong()
        }
        if (filters.dateFrom) {
            query += " AND t.date_from >= ? "
            params << filters.dateFrom
        }
        if (filters.dateTo) {
            query += " AND t.date_to <= ? "
            params << filters.dateTo
        }
        query += " ORDER BY i.name, d.name, e.name, t.date_from DESC"

        def rows = sql.rows(query, params)
        sql.close()
        return rows.collect { row ->
            [
                institutionId   : row.institution_id,
                institutionName : row.institution_name,
                departmentId    : row.department_id,
                departmentName  : row.department_name,
                employeeId      : row.employee_id,
                empNo           : row.emp_no,
                employeeName    : row.employee_name,
                taskName        : row.task_name,
                taskType        : row.task_type,
                dateFrom        : row.date_from?.toString(),
                dateTo          : row.date_to?.toString(),
                hoursSpent      : row.hours_spent,
                deptDailyHours  : row.dept_daily_hours,
                description     : row.description,
                dateCreated     : row.date_created?.toString()
            ]
        }
    }

    // ----------------------------------------------------------
    // Notification Logs Report
    // ----------------------------------------------------------
    List getNotificationLogs(Map filters) {
        def sql = new Sql(dataSource)
        def query = """
            SELECT
                nl.id       AS log_id,
                e.emp_no    AS emp_no,
                e.name      AS employee_name,
                d.name      AS department_name,
                nl.subject,
                nl.message,
                nl.status,
                nl.sent_at
            FROM notification_log nl
            LEFT JOIN employee   e ON nl.employee_id = e.id
            LEFT JOIN department d ON e.department_id = d.id
            WHERE 1 = 1
        """
        def params = []

        if (filters.employeeId) {
            query += " AND nl.employee_id = ? "
            params << filters.employeeId.toLong()
        }
        if (filters.status) {
            query += " AND nl.status = ? "
            params << filters.status
        }
        if (filters.dateFrom) {
            query += " AND DATE(nl.sent_at) >= ? "
            params << filters.dateFrom
        }
        if (filters.dateTo) {
            query += " AND DATE(nl.sent_at) <= ? "
            params << filters.dateTo
        }
        query += " ORDER BY nl.sent_at DESC"

        def rows = sql.rows(query, params)
        sql.close()
        return rows.collect { row ->
            [
                logId          : row.log_id,
                empNo          : row.emp_no,
                employeeName   : row.employee_name,
                departmentName : row.department_name,
                subject        : row.subject,
                message        : row.message,
                status         : row.status,
                sentAt         : row.sent_at?.toString()
            ]
        }
    }
}