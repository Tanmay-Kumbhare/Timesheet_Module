<template>
  <aside class="sidebar">
    <div class="timesheet-title">Timesheet</div>
    <div class="search-feature">Search Feature</div>
    <nav>
  <router-link to="/timesheet/entry" class="nav-link">📝 Submit Timesheet</router-link>
  <router-link to="/hod/dashboard" class="nav-link">👑 HOD Dashboard</router-link>
  <router-link to="/timesheet/master/taskmaster" class="nav-link">📋 Task Master</router-link>
  <router-link to="/timesheet/master/tasktype" class="nav-link">🏷️ Task Types</router-link>
  <router-link to="/timesheet/reports" class="nav-link">📊 Reports</router-link>
</nav>
    <div class="footer">
      Powered By<br />
      <strong>EduplusCampus</strong>
    </div>
  </aside>
</template>

<style scoped>
.sidebar {
  width: 260px;
  background: linear-gradient(135deg, #fff 0%, #f8f9fa 100%);
  box-shadow: 2px 0 12px rgba(0,0,0,0.08);
  padding: 25px 20px;
  display: flex;
  flex-direction: column;
  gap: 25px;
  min-height: 100vh;
}
.timesheet-title {
  font-size: 1.6rem;
  font-weight: bold;
  color: #1a237e;
  border-bottom: 3px solid #1a237e;
  padding-bottom: 10px;
}
.search-feature {
  background: #e8eaf6;
  padding: 10px;
  border-radius: 8px;
  color: #1a237e;
  font-weight: 500;
}
.nav-link {
  display: block;
  padding: 12px 15px;
  color: #333;
  text-decoration: none;
  border-radius: 8px;
  margin: 8px 0;
  transition: all 0.3s ease;
  font-weight: 500;
}
.nav-link:hover {
  background: #1a237e;
  color: white;
  transform: translateX(5px);
}
.router-link-active {
  background: #1a237e;
  color: white;
}
.footer {
  margin-top: auto;
  font-size: 0.8rem;
  color: #666;
  text-align: center;
  padding-top: 20px;
  border-top: 1px solid #e0e0e0;
}
</style>