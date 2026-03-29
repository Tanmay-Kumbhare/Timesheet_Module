// ============================================================
// FILE: src/main.js
// ============================================================
import { createApp } from 'vue'
import App from './App.vue'
import router from './router/index.js'

createApp(App).use(router).mount('#app')


// ============================================================
// FILE: src/App.vue
// ============================================================
<template>
  <div id="app">
    <AppHeader />
    <RouterView />
    <AppFooter />
  </div>
</template>

<script setup>
import AppHeader from '@/components/AppHeader.vue'
import AppFooter from '@/components/AppFooter.vue'
</script>

<style>
* { box-sizing: border-box; margin: 0; padding: 0; }
body { font-family: Arial, sans-serif; font-size: 14px; color: #333; background: #fff; }
a { color: #1a6fc4; text-decoration: none; }
a:hover { text-decoration: underline; }
table { border-collapse: collapse; width: 100%; }
th, td { border: 1px solid #ccc; padding: 8px 12px; text-align: left; }
th { background: #f0f0f0; font-weight: bold; }
tr:hover td { background: #fafafa; }
button { cursor: pointer; }
input, select, textarea {
  border: 1px solid #ccc; border-radius: 3px;
  padding: 6px 10px; font-size: 14px; width: 100%;
}
</style>


// ============================================================
// FILE: src/components/AppHeader.vue
// ============================================================
<template>
  <header class="app-header">
    <div class="header-top">
      <div class="logo-area">
        <span class="logo-text">V</span>
        <div>
          <div class="inst-name">Vishwakarma Institute of Technology, Pune</div>
          <div class="network-info">You are on 4G network, Your internet Speed: 10+ Mbps</div>
        </div>
      </div>
      <div class="user-area">👤 {{ employee.name }}</div>
    </div>
    <div class="breadcrumb">
      <RouterLink to="/">Home</RouterLink>
      <span v-for="(crumb, i) in breadcrumbs" :key="i">
        &nbsp;-&gt;&nbsp;
        <RouterLink v-if="crumb.to" :to="crumb.to">{{ crumb.label }}</RouterLink>
        <span v-else>{{ crumb.label }}</span>
      </span>
    </div>
    <div class="employee-bar">
      <span>Employee No: {{ employee.empNo }}</span>
      <span style="margin: 0 40px">{{ employee.name }}</span>
      <span>{{ employee.departmentName }}</span>
    </div>
  </header>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

// Simulated logged-in employee (replace with auth context in real app)
const employee = {
  empNo: '10791',
  name: 'DIPAK PAWAR',
  departmentName: 'Department of Computer Engineering'
}

const route = useRoute()
const breadcrumbs = computed(() => {
  const map = {
    '/timesheet': [{ label: 'Timesheet' }],
    '/timesheet/master': [{ label: 'Timesheet', to: '/timesheet' }, { label: 'Master' }],
    '/timesheet/master/task-types': [{ label: 'Timesheet', to: '/timesheet' }, { label: 'Task Type' }],
    '/timesheet/master/task-master': [{ label: 'Timesheet', to: '/timesheet' }, { label: 'Task Master' }],
    '/timesheet/master/notifications': [{ label: 'Timesheet', to: '/timesheet' }, { label: 'Notification Settings' }],
    '/timesheet/reports': [{ label: 'Reports' }],
    '/timesheet/reports/departmentwise': [{ label: 'Reports', to: '/timesheet/reports' }, { label: 'Departmentwise' }],
    '/timesheet/reports/institutewise': [{ label: 'Reports', to: '/timesheet/reports' }, { label: 'Institutewise' }],
    '/timesheet/reports/notification-logs': [{ label: 'Reports', to: '/timesheet/reports' }, { label: 'Notification Logs' }],
  }
  return map[route.path] || []
})
</script>

<style scoped>
.app-header { border-bottom: 2px solid #00008b; }
.header-top {
  display: flex; justify-content: space-between; align-items: center;
  padding: 8px 20px; background: #fff;
}
.logo-text {
  font-size: 36px; font-weight: bold; color: #00008b;
  margin-right: 10px; border: 2px solid #00008b; padding: 2px 8px;
}
.inst-name { font-weight: bold; font-size: 15px; color: #00008b; }
.network-info { font-size: 12px; color: #e60000; font-weight: bold; }
.user-area { font-size: 13px; }
.breadcrumb { padding: 4px 20px; font-size: 13px; background: #f8f8f8; }
.employee-bar {
  padding: 6px 20px; font-size: 13px; border-top: 1px solid #ccc;
  display: flex; gap: 20px;
}
</style>


// ============================================================
// FILE: src/components/AppFooter.vue
// ============================================================
<template>
  <footer class="app-footer">
    Powered By <a href="#">EduplusCampus</a> &nbsp;|&nbsp; eduplus
  </footer>
</template>

<style scoped>
.app-footer {
  text-align: center; padding: 12px; font-size: 12px;
  border-top: 1px solid #ccc; margin-top: 40px; color: #666;
}
</style>


// ============================================================
// FILE: src/components/SidebarNav.vue
// Reusable left sidebar shown on Coordinator pages (wireframe img 2/3/4)
// ============================================================
<template>
  <aside class="sidebar">
    <div class="search-box">
      <input v-model="searchTerm" placeholder="Search Feature" />
      <span class="search-icon">🔍</span>
    </div>
    <RouterLink to="/timesheet/master" class="sidebar-btn" :class="{ active: isMaster }">
      <span class="icon">🗄️</span> Timesheet Master
    </RouterLink>
    <RouterLink to="/timesheet/reports" class="sidebar-btn" :class="{ active: isReports }">
      <span class="icon">📋</span> Timesheet Reports
    </RouterLink>
  </aside>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'

const searchTerm = ref('')
const route = useRoute()
const isMaster  = computed(() => route.path.includes('/master'))
const isReports = computed(() => route.path.includes('/reports'))
</script>

<style scoped>
.sidebar { width: 230px; min-width: 230px; padding: 12px; }
.search-box { display: flex; border: 1px solid #ccc; border-radius: 3px; overflow: hidden; margin-bottom: 10px; }
.search-box input { border: none; outline: none; flex: 1; padding: 6px 8px; }
.search-icon { padding: 6px 8px; background: #f0f0f0; }
.sidebar-btn {
  display: flex; align-items: center; gap: 10px;
  padding: 10px 14px; margin-bottom: 6px; border: 1px solid #aaa;
  border-radius: 4px; color: #333; font-weight: 500;
  text-decoration: none; background: #fff;
}
.sidebar-btn:hover, .sidebar-btn.active {
  background: #d070d0; color: #fff; border-color: #d070d0;
}
.icon { font-size: 16px; }
</style>
