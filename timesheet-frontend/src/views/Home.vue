<template>
  <div class="home">
    <h1>Welcome to Timesheet Management System</h1>
    <p>Vishwakarma Institute of Technology, Pune</p>
    <div class="quick-links">
      <router-link to="/timesheet/entry" class="link-card">
        <h3>Submit Timesheet</h3>
        <p>Log your daily work hours</p>
      </router-link>
      <router-link to="/timesheet/master/taskmaster" class="link-card">
        <h3>Task Master</h3>
        <p>Manage tasks and assignments</p>
      </router-link>
      <router-link to="/timesheet/reports" class="link-card">
        <h3>Reports</h3>
        <p>View timesheet reports</p>
      </router-link>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Home'
}
</script>

<style scoped>
.home {
  padding: 40px;
  text-align: center;
}
.home h1 {
  color: #1a237e;
  margin-bottom: 10px;
}
.quick-links {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-top: 40px;
}
.link-card {
  background: white;
  padding: 30px;
  border-radius: 12px;
  text-decoration: none;
  color: #333;
  transition: transform 0.2s, box-shadow 0.2s;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}
.link-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0,0,0,0.2);
}
.link-card h3 {
  color: #1a237e;
  margin-bottom: 10px;
}
</style>