# 📊 Timesheet Management System

> A comprehensive timesheet management system for educational institutions, built with **Grails 7.0.8** (Backend) and **Vue.js 3** (Frontend).

[![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Grails](https://img.shields.io/badge/Grails-7.0.8-brightgreen.svg)](https://grails.org/)
[![Vue.js](https://img.shields.io/badge/Vue.js-3.x-4fc08d.svg)](https://vuejs.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-orange.svg)](https://mysql.com/)

---

## 🏫 Institution
**Vishwakarma Institute of Technology, Pune**  
*Department of Computer Engineering*

---

## 📋 Features

### ✅ Completed Features

| Module | Features |
|--------|----------|
| **Task Management** | Create, edit, remove task categories & tasks with category assignment |
| **Timesheet Entry** | Submit daily work with date, time, description; auto hours calculation |
| **History View** | View all submitted timesheets with real-time status tracking |
| **HOD Dashboard** | Approve/Reject timesheets with remarks and comments |
| **Reports Module** | Filter by department, status, date range; export to CSV |
| **Analytics** | Real-time summary cards (Total, Pending, Approved, Rejected) |

### 🔄 Roadmap

- [ ] User Authentication (Login/Logout)
- [ ] Email Notifications (Approval/Rejection alerts)
- [ ] Calendar View (Visual timesheet display)
- [ ] Monthly Reports Dashboard
- [ ] Mobile Responsive Design
- [ ] PDF Export

---

## 🛠️ Tech Stack

### Backend
| Technology | Version | Purpose |
|------------|---------|---------|
| **Grails** | 7.0.8 | Web Framework |
| **Groovy** | 3.0.x | Programming Language |
| **MySQL** | 8.0 | Database |
| **Gradle** | 7.x | Build Tool |
| **Hibernate** | 6.x | ORM |

### Frontend
| Technology | Version | Purpose |
|------------|---------|---------|
| **Vue.js** | 3.x | Frontend Framework |
| **Vite** | 7.x | Build Tool |
| **Axios** | 1.x | HTTP Client |
| **Vue Router** | 4.x | Routing |

---

## 🚀 Quick Start

### Prerequisites
- Java 11 or 17
- Grails 7.0.8
- Node.js 16+
- MySQL 8.0

### Installation

#### 1. Clone the Repository

```bash
git clone https://github.com/Tanmay-Kumbhare/Timesheet_Module.git
cd Timesheet_Module
```

#### 2. Database Setup

```sql
CREATE DATABASE IF NOT EXISTS timesheet_db 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE timesheet_db;
```

#### 3. Backend Setup

```bash
cd timesheet-backend
grails clean
grails run-app
```

> Backend runs at: http://localhost:8080

#### 4. Frontend Setup

```bash
cd timesheet-frontend
npm install
npm run dev
```

> Frontend runs at: http://localhost:5173
