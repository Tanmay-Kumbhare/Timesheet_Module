# Timesheet Management System

A comprehensive timesheet management system for educational institutions, built with **Grails 7.0.8** (Backend) and **Vue.js 3** (Frontend).

## 🏫 Institution
**Vishwakarma Institute of Technology, Pune**

## 📋 Features

### ✅ Completed Features
- **Task Categories Management** - Create, edit, remove task categories
- **Task Management** - Create, edit, remove tasks with category assignment
- **Timesheet Submission** - Submit daily work with date, time, description
- **Auto Hours Calculation** - Automatic calculation from start/end time
- **My Timesheets History** - View all submitted timesheets with status
- **HOD Dashboard** - Approve/Reject timesheets with remarks
- **Reports Module** - Filter by department, status, date range
- **Export to CSV** - Download reports in CSV format
- **Summary Cards** - Real-time statistics (Total, Pending, Approved, Rejected)

### 🔄 In Progress
- User Authentication
- Email Notifications
- Calendar View
- Monthly Reports Dashboard

## 🛠️ Tech Stack

### Backend
| Technology | Version | Purpose |
|------------|---------|---------|
| Grails | 7.0.8 | Web Framework |
| Groovy | 3.0.x | Programming Language |
| MySQL | 8.0 | Database |
| Gradle | 7.x | Build Tool |

### Frontend
| Technology | Version | Purpose |
|------------|---------|---------|
| Vue.js | 3.x | Frontend Framework |
| Vite | 7.x | Build Tool |
| Axios | 1.x | HTTP Client |
| Vue Router | 4.x | Routing |

## 🚀 Quick Start

### Prerequisites
- Java 11 or 17
- Grails 7.0.8
- Node.js 16+
- MySQL 8.0

Final Repository Structure

Timesheet_Module/
├── timesheet-backend/              # Grails 7.0.8 Backend
├── timesheet-frontend/             # Vue.js 3 Frontend
├── database/
│   └── schema.sql                  # Complete database schema
├── README.md                       # Project documentation ✅
├── ARCHITECTURE.md                 # System architecture & API reference ✅
├── LICENSE                         # MIT License
├── DEPLOYMENT.md                   # Production deployment guide
├── .env.example                    # Environment variables template
└── .gitignore                      # Git ignore rules

### Database Setup

```sql
-- Create database
CREATE DATABASE IF NOT EXISTS timesheet_db 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE timesheet_db;

-- Run all table creation scripts (provided in /database/schema.sql)

