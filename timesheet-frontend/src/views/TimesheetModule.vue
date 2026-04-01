<template>
  <div class="page">
    <h2>Timesheet Module</h2>

    <div class="cards">
      <div class="card" @click="$router.push('/timesheet/master')">
        Timesheet Master
      </div>

      <div class="card" @click="$router.push('/timesheet/reports')">
        Timesheet Reports
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "TimesheetModule"
}
</script>

<style scoped>
.cards {
  display: flex;
  gap: 30px;
  margin-top: 20px;
}
.card {
  padding: 30px;
  border: 1px solid #ccc;
  cursor: pointer;
}
</style>