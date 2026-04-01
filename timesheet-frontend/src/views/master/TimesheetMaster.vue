<template>
  <div class="master-container">
    <h3>Master Data - Timesheet</h3>
    <table class="data-table">
      <thead>
        <tr><th>Sr. No.</th><th>Module</th><th>Table Name</th><th>Records</th><th>Action</th></tr>
      </thead>
      <tbody>
        <tr v-for="(item, idx) in masterItems" :key="idx">
          <td>{{ idx + 1 }}</td>
          <td>{{ item.module }}</td>
          <td>{{ item.tableName }}</td>
          <td>{{ item.records }}</td>
          <td><button @click="navigateTo(item.route)">OPEN PAGE</button></td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  data() {
    return {
      masterItems: [
        { module: 'Timesheet', tableName: 'Task Types', records: 10, route: '/timesheet/master/tasktype' },
        { module: 'Timesheet', tableName: 'Task Master', records: 10, route: '/timesheet/master/taskmaster' },
        { module: 'Timesheet', tableName: 'Notification Settings', records: '-', route: '/timesheet/master/notifications' },
      ]
    }
  },
  methods: {
    navigateTo(route) { this.$router.push(route); }
  }
}
</script>

<style scoped>
.data-table { width: 100%; border-collapse: collapse; margin-top: 20px; }
.data-table th, .data-table td { border: 1px solid #ddd; padding: 10px; text-align: left; }
.data-table th { background: #1a237e; color: white; }
button { background: #1a237e; color: white; border: none; padding: 6px 12px; border-radius: 4px; cursor: pointer; }
</style>