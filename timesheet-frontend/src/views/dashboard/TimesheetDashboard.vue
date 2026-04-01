// ============================================================
// FILE: src/views/TimesheetDashboard.vue
// Wireframe Image 1: Entry page with Coordinator button
// ============================================================
<template>
  <div class="dashboard-page">
    <h2 class="page-title">Timesheet</h2>
    <div class="tiles">
      <RouterLink to="/timesheet/master" class="tile">
        <div class="tile-icon">👥</div>
        <div class="tile-label">Coordinator</div>
      </RouterLink>
    </div>
  </div>
</template>

<style scoped>
.dashboard-page { padding: 30px 40px; }
.page-title { text-align: center; margin-bottom: 30px; font-size: 18px; }
.tiles { display: flex; gap: 20px; flex-wrap: wrap; }
.tile {
  display: flex; flex-direction: column; align-items: center; justify-content: center;
  width: 120px; height: 100px; border: 2px solid #00008b; border-radius: 6px;
  text-decoration: none; color: #333;
}
.tile:hover { background: #f0f4ff; }
.tile-icon { font-size: 36px; margin-bottom: 6px; }
.tile-label { font-size: 13px; font-weight: bold; }
</style>

