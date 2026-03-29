-- ============================================================
-- Timesheet Management System - MySQL Schema
-- Vishwakarma Institute of Technology, Pune
-- ============================================================

CREATE DATABASE IF NOT EXISTS timesheet_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE timesheet_db;

-- ------------------------------------------------------------
-- 1. Institution
-- ------------------------------------------------------------
CREATE TABLE institution (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    code          VARCHAR(50)  NOT NULL UNIQUE,
    is_active     BOOLEAN      NOT NULL DEFAULT TRUE,
    date_created  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ------------------------------------------------------------
-- 2. Department
-- ------------------------------------------------------------
CREATE TABLE department (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(255) NOT NULL,
    code           VARCHAR(50)  NOT NULL UNIQUE,
    institution_id BIGINT       NOT NULL,
    is_active      BOOLEAN      NOT NULL DEFAULT TRUE,
    date_created   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_dept_institution FOREIGN KEY (institution_id) REFERENCES institution(id)
);

-- ------------------------------------------------------------
-- 3. Employee
-- ------------------------------------------------------------
CREATE TABLE employee (
    id            BIGINT       AUTO_INCREMENT PRIMARY KEY,
    emp_no        VARCHAR(50)  NOT NULL UNIQUE,
    name          VARCHAR(255) NOT NULL,
    email         VARCHAR(255),
    department_id BIGINT       NOT NULL,
    role          VARCHAR(50)  NOT NULL DEFAULT 'EMPLOYEE',
    -- roles: EMPLOYEE | COORDINATOR | HOD
    is_active     BOOLEAN      NOT NULL DEFAULT TRUE,
    date_created  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_emp_department FOREIGN KEY (department_id) REFERENCES department(id)
);

-- ------------------------------------------------------------
-- 4. Task Type  (ERPTaskMasterType in wireframe)
-- ------------------------------------------------------------
CREATE TABLE task_type (
    id           BIGINT       AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    is_active    BOOLEAN      NOT NULL DEFAULT TRUE,
    date_created DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ------------------------------------------------------------
-- 5. Task Master
-- ------------------------------------------------------------
CREATE TABLE task_master (
    id           BIGINT       AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    task_type_id BIGINT       NOT NULL,
    is_active    BOOLEAN      NOT NULL DEFAULT TRUE,
    date_created DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_task_type FOREIGN KEY (task_type_id) REFERENCES task_type(id)
);

-- ------------------------------------------------------------
-- 6. Notification Settings
-- ------------------------------------------------------------
CREATE TABLE notification_settings (
    id                 BIGINT       AUTO_INCREMENT PRIMARY KEY,
    notification_type  VARCHAR(100) NOT NULL,
    -- values: EMAIL_REMINDER | APPROVAL_ALERT
    recipient_role     VARCHAR(50)  NOT NULL,
    -- values: EMPLOYEE | HOD | COORDINATOR
    subject            VARCHAR(255) NOT NULL,
    message_template   TEXT         NOT NULL,
    is_active          BOOLEAN      NOT NULL DEFAULT TRUE,
    date_created       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ------------------------------------------------------------
-- 7. Timesheet  (one record per task submission)
-- ------------------------------------------------------------
CREATE TABLE timesheet (
    id             BIGINT         AUTO_INCREMENT PRIMARY KEY,
    employee_id    BIGINT         NOT NULL,
    task_master_id BIGINT         NOT NULL,
    date_from      DATE           NOT NULL,
    date_to        DATE           NOT NULL,
    hours_spent    DECIMAL(6, 2)  NOT NULL,
    description    TEXT,
    date_created   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_ts_employee   FOREIGN KEY (employee_id)    REFERENCES employee(id),
    CONSTRAINT fk_ts_task       FOREIGN KEY (task_master_id) REFERENCES task_master(id)
);

-- ------------------------------------------------------------
-- 8. Notification Log
-- ------------------------------------------------------------
CREATE TABLE notification_log (
    id                      BIGINT       AUTO_INCREMENT PRIMARY KEY,
    notification_settings_id BIGINT,
    employee_id             BIGINT,
    subject                 VARCHAR(255),
    message                 TEXT,
    status                  VARCHAR(50)  NOT NULL DEFAULT 'SENT',
    -- values: SENT | FAILED
    sent_at                 DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_created            DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated            DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_log_notif_setting FOREIGN KEY (notification_settings_id) REFERENCES notification_settings(id),
    CONSTRAINT fk_log_employee      FOREIGN KEY (employee_id)               REFERENCES employee(id)
);

-- ------------------------------------------------------------
-- Sample seed data
-- ------------------------------------------------------------
INSERT INTO institution (name, code) VALUES ('Vishwakarma Institute of Technology', 'VIT');

INSERT INTO department (name, code, institution_id) VALUES
    ('Computer Engineering',   'CE',   1),
    ('Mechanical Engineering', 'ME',   1),
    ('Electronics Engineering','EE',   1);

INSERT INTO employee (emp_no, name, email, department_id, role) VALUES
    ('10791', 'Dipak Pawar',   'dipak.pawar@vit.edu',   1, 'COORDINATOR'),
    ('10001', 'Rahul Sharma',  'rahul.sharma@vit.edu',  1, 'EMPLOYEE'),
    ('10002', 'Priya Singh',   'priya.singh@vit.edu',   2, 'EMPLOYEE'),
    ('10003', 'Amit Joshi',    'amit.joshi@vit.edu',    1, 'HOD');

INSERT INTO task_type (name) VALUES
    ('Teaching'),
    ('Research'),
    ('Administrative'),
    ('Workshop');

INSERT INTO task_master (name, task_type_id) VALUES
    ('Lecture Delivery',       1),
    ('Lab Supervision',        1),
    ('Research Paper Writing', 2),
    ('Project Guidance',       2),
    ('Department Meeting',     3),
    ('Workshop Conduction',    4);

INSERT INTO notification_settings (notification_type, recipient_role, subject, message_template) VALUES
    ('EMAIL_REMINDER', 'EMPLOYEE',    'Timesheet Submission Reminder',
     'Dear {employee_name}, please submit your timesheet for {period} by end of day.'),
    ('APPROVAL_ALERT', 'EMPLOYEE',    'Timesheet Submission Confirmation',
     'Dear {employee_name}, your timesheet dated {date} has been received successfully.');
