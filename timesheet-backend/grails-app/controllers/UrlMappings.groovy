// ============================================================
// FILE: grails-app/conf/UrlMappings.groovy
// ============================================================
class UrlMappings {
    static mappings = {
        // --- Master APIs ---
        "/api/taskTypes"(resources: 'taskType')
        "/api/taskMasters"(resources: 'taskMaster')
        "/api/notificationSettings"(resources: 'notificationSettings')

        // --- Timesheet APIs ---
        "/api/timesheets"(resources: 'timesheet')

        // --- Notification Log APIs ---
        "/api/notificationLogs"(resources: 'notificationLog')

        // --- Reference / lookup APIs ---
        "/api/institutions"(resources: 'institution')
        "/api/departments"(resources: 'department')
        "/api/employees"(resources: 'employee')

        // --- Report APIs ---
        "/api/reports/departmentwise"(controller: 'report', action: 'departmentwise', method: 'GET')
        "/api/reports/institutewise"(controller: 'report', action: 'institutewise',  method: 'GET')
        "/api/reports/notificationLogs"(controller: 'report', action: 'notificationLogs', method: 'GET')

        // --- Master index (count of records per master table) ---
        "/api/masterIndex"(controller: 'masterIndex', action: 'index', method: 'GET')

        "/"(view: "/index")
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}


// ============================================================
// FILE: grails-app/conf/application.yml  (MySQL datasource)
// ============================================================
/*
dataSource:
    pooled: true
    jmxExport: true
    driverClassName: com.mysql.cj.jdbc.Driver
    dialect: org.hibernate.dialect.MySQL8Dialect

environments:
    development:
        dataSource:
            url: jdbc:mysql://localhost:3306/timesheet_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
            username: root
            password: your_password_here
            dbCreate: update
    production:
        dataSource:
            url: jdbc:mysql://localhost:3306/timesheet_db?useSSL=true&serverTimezone=UTC
            username: timesheet_user
            password: your_secure_password
            dbCreate: none

grails:
    cors:
        enabled: true
        allowedOrigins:
            - "http://localhost:5173"   # Vue dev server
        allowedMethods:
            - GET
            - POST
            - PUT
            - DELETE
            - OPTIONS
        allowedHeaders:
            - Content-Type
            - Authorization
*/
