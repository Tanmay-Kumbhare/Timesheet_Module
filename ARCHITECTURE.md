# Timesheet Management System
## Complete Architecture & API Reference
### Vishwakarma Institute of Technology, Pune

---

## 1. Project Structure

```
timesheet-system/
├── grails-backend/                    # Grails 6 project
│   ├── grails-app/
│   │   ├── domain/com/vit/timesheet/
│   │   │   ├── Institution.groovy
│   │   │   ├── Department.groovy
│   │   │   ├── Employee.groovy
│   │   │   ├── TaskType.groovy        ← ERPTaskMasterType (wireframe label)
│   │   │   ├── TaskMaster.groovy
│   │   │   ├── NotificationSettings.groovy
│   │   │   ├── Timesheet.groovy
│   │   │   └── NotificationLog.groovy
│   │   ├── controllers/com/vit/timesheet/
│   │   │   ├── TaskTypeController.groovy
│   │   │   ├── TaskMasterController.groovy
│   │   │   ├── NotificationSettingsController.groovy
│   │   │   ├── TimesheetController.groovy
│   │   │   ├── NotificationLogController.groovy
│   │   │   ├── MasterIndexController.groovy
│   │   │   ├── ReportController.groovy
│   │   │   ├── InstitutionController.groovy
│   │   │   ├── DepartmentController.groovy
│   │   │   └── EmployeeController.groovy
│   │   ├── services/com/vit/timesheet/
│   │   │   ├── TimesheetService.groovy
│   │   │   └── ReportService.groovy
│   │   └── conf/
│   │       ├── UrlMappings.groovy
│   │       └── application.yml
│   └── build.gradle
│
└── vue-frontend/                      # Vue 3 project
    ├── src/
    │   ├── main.js
    │   ├── App.vue
    │   ├── router/index.js
    │   ├── api/index.js
    │   ├── components/
    │   │   ├── AppHeader.vue
    │   │   ├── AppFooter.vue
    │   │   └── SidebarNav.vue
    │   └── views/
    │       ├── TimesheetDashboard.vue     ← Wireframe Image 1
    │       ├── TimesheetModule.vue        ← Wireframe Image 2
    │       ├── master/
    │       │   ├── TimesheetMaster.vue    ← Wireframe Image 3
    │       │   ├── TaskTypeManagement.vue
    │       │   ├── TaskMasterManagement.vue
    │       │   └── NotificationSettings.vue
    │       └── reports/
    │           ├── TimesheetReports.vue   ← Wireframe Image 4 (tab container)
    │           ├── DepartmentwiseReport.vue
    │           ├── InstitutewiseReport.vue
    │           └── NotificationLogs.vue
    └── package.json
```

---

## 2. Database Entity Relationship

```
institution  ──< department ──< employee ──< timesheet >── task_master >── task_type
                                         \
                                          └── notification_log >── notification_settings
```

| Table                 | PK  | Key FKs                              |
|-----------------------|-----|--------------------------------------|
| institution           | id  | —                                    |
| department            | id  | institution_id → institution         |
| employee              | id  | department_id → department           |
| task_type             | id  | —                                    |
| task_master           | id  | task_type_id → task_type             |
| timesheet             | id  | employee_id, task_master_id          |
| notification_settings | id  | —                                    |
| notification_log      | id  | employee_id, notification_settings_id|

---

## 3. REST API Endpoint Reference

### Master Data APIs

| Method | Endpoint                        | Description                          |
|--------|---------------------------------|--------------------------------------|
| GET    | /api/masterIndex                | List of master tables with counts    |
| GET    | /api/taskTypes                  | List all active task types           |
| POST   | /api/taskTypes                  | Create task type                     |
| PUT    | /api/taskTypes/{id}             | Update task type                     |
| DELETE | /api/taskTypes/{id}             | Soft-delete task type                |
| GET    | /api/taskMasters                | List all active tasks                |
| POST   | /api/taskMasters                | Create task                          |
| PUT    | /api/taskMasters/{id}           | Update task                          |
| DELETE | /api/taskMasters/{id}           | Soft-delete task                     |
| GET    | /api/notificationSettings       | List all active notification settings|
| POST   | /api/notificationSettings       | Create notification setting          |
| PUT    | /api/notificationSettings/{id}  | Update notification setting          |
| DELETE | /api/notificationSettings/{id}  | Soft-delete notification setting     |

### Timesheet APIs

| Method | Endpoint                         | Description                           |
|--------|----------------------------------|---------------------------------------|
| GET    | /api/timesheets                  | List timesheets (filterable)          |
| POST   | /api/timesheets                  | Submit a new timesheet entry          |
| PUT    | /api/timesheets/{id}             | Update timesheet entry                |
| DELETE | /api/timesheets/{id}             | Delete timesheet entry                |

**GET /api/timesheets query params:**
- `employeeId` – filter by employee
- `taskMasterId` – filter by task
- `dateFrom` – filter entries on/after this date (yyyy-MM-dd)
- `dateTo` – filter entries on/before this date (yyyy-MM-dd)

**POST /api/timesheets body:**
```json
{
  "employeeId":   1,
  "taskMasterId": 3,
  "dateFrom":     "2025-01-01",
  "dateTo":       "2025-01-05",
  "hoursSpent":   8.5,
  "description":  "Delivered lecture on OS scheduling"
}
```

### Reference APIs

| Method | Endpoint                            | Description                              |
|--------|-------------------------------------|------------------------------------------|
| GET    | /api/institutions                   | List all institutions                    |
| GET    | /api/departments?institutionId=     | List departments (optionally filtered)   |
| GET    | /api/employees?departmentId=        | List employees (optionally filtered)     |
| GET    | /api/notificationLogs               | List notification logs                   |

### Report APIs

| Method | Endpoint                          | Description                             |
|--------|-----------------------------------|-----------------------------------------|
| GET    | /api/reports/departmentwise       | Departmentwise report (HOD view)        |
| GET    | /api/reports/institutewise        | Institutewise report (admin view)       |
| GET    | /api/reports/notificationLogs     | Notification logs report                |

**GET /api/reports/departmentwise params:**
- `departmentId`, `employeeId`, `dateFrom`, `dateTo`

**GET /api/reports/institutewise params:**
- `institutionId`, `departmentId`, `dateFrom`, `dateTo`

**GET /api/reports/notificationLogs params:**
- `employeeId`, `status` (SENT|FAILED), `dateFrom`, `dateTo`

---

## 4. Vue Router Map

| Route Path                               | Component                  | Wireframe    |
|------------------------------------------|----------------------------|--------------|
| /                                        | TimesheetDashboard         | Image 1      |
| /timesheet                               | TimesheetModule            | Image 2      |
| /timesheet/master                        | TimesheetMaster            | Image 3      |
| /timesheet/master/task-types             | TaskTypeManagement         | OPEN PAGE #1 |
| /timesheet/master/task-master            | TaskMasterManagement       | OPEN PAGE #2 |
| /timesheet/master/notifications          | NotificationSettings       | OPEN PAGE #3 |
| /timesheet/reports                       | TimesheetReports           | Image 4      |
| /timesheet/reports/departmentwise        | DepartmentwiseReport       | Image 4 tab1 |
| /timesheet/reports/institutewise         | InstitutewiseReport        | Image 4 tab2 |
| /timesheet/reports/notification-logs     | NotificationLogs           | Image 4 tab3 |

---

## 5. Timesheet Submission Workflow

```
Employee fills form
        │
        ▼
POST /api/timesheets
        │
        ▼
TimesheetController.save()
        │
        ├── Validates employee + taskMaster exist
        ├── Creates Timesheet record
        │
        ▼
TimesheetService.triggerNotification()
        │
        ├── Queries NotificationSettings WHERE type = APPROVAL_ALERT
        ├── Substitutes {employee_name}, {date} in message template
        └── Creates NotificationLog record (status = SENT)
```

> **Note:** Actual email dispatch (JavaMailSender or similar) is not implemented as
> no SMTP configuration was specified. The log record acts as the confirmation trail.
> Wire up `grails.mail` plugin when SMTP details are available.

---

## 6. Report Queries (Raw SQL Reference)

### Departmentwise Daily Report (dailytimesheetreportforHOD equivalent)

```sql
SELECT
    e.emp_no, e.name AS employee_name,
    d.name  AS department_name,
    tm.name AS task_name, tt.name AS task_type,
    t.date_from, t.date_to, t.hours_spent, t.description
FROM   timesheet t
JOIN   employee    e  ON t.employee_id    = e.id
JOIN   department  d  ON e.department_id  = d.id
JOIN   task_master tm ON t.task_master_id = tm.id
JOIN   task_type   tt ON tm.task_type_id  = tt.id
WHERE  d.id = :departmentId           -- optional
  AND  e.id = :employeeId             -- optional
  AND  t.date_from >= :dateFrom       -- optional
  AND  t.date_to   <= :dateTo         -- optional
ORDER BY d.name, e.name, t.date_from DESC;
```

### Institutewise Daily Report (dailytimesheetreportnew equivalent)

```sql
SELECT
    i.name AS institution_name,
    d.name AS department_name,
    e.emp_no, e.name AS employee_name,
    tm.name AS task_name, tt.name AS task_type,
    t.date_from, t.date_to, t.hours_spent,
    SUM(t.hours_spent) OVER (PARTITION BY d.id, t.date_from) AS dept_daily_hours
FROM   timesheet t
JOIN   employee    e  ON t.employee_id    = e.id
JOIN   department  d  ON e.department_id  = d.id
JOIN   institution i  ON d.institution_id = i.id
JOIN   task_master tm ON t.task_master_id = tm.id
JOIN   task_type   tt ON tm.task_type_id  = tt.id
WHERE  i.id = :institutionId          -- optional
  AND  d.id = :departmentId           -- optional
  AND  t.date_from >= :dateFrom       -- optional
  AND  t.date_to   <= :dateTo         -- optional
ORDER BY i.name, d.name, e.name, t.date_from DESC;
```

---

## 7. Grails build.gradle dependencies to add

```groovy
dependencies {
    // MySQL driver
    runtimeOnly 'com.mysql:mysql-connector-j:8.2.0'
    // Grails REST support
    compile 'org.grails.plugins:grails-plugin-rest'
    // CORS support
    compile 'org.grails.plugins:cors'
}
```

## 8. Vue package.json dependencies

```json
{
  "dependencies": {
    "vue":        "^3.4.0",
    "vue-router": "^4.3.0"
  },
  "devDependencies": {
    "@vitejs/plugin-vue": "^5.0.0",
    "vite":               "^5.0.0"
  }
}
```

---

## 9. Notes & Open Items

| Item | Status | Note |
|------|--------|------|
| Approval workflow | ⏳ Deferred | Not confirmed – no status field on Timesheet currently. Add `status VARCHAR(50)` column and approval endpoints when confirmed. |
| Email dispatch   | ⏳ Deferred | Add `grails.mail` plugin + SMTP config when available. |
| Authentication   | ⏳ Excluded | Not requested. Employee context currently hardcoded to empNo 10791. |
| Pagination       | ⏳ Optional | Add `?max=20&offset=0` params to list endpoints when data grows. |
