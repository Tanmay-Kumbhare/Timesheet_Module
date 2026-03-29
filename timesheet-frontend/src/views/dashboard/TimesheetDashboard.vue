<template>
  <div class="container">
    <h1>Timesheet Module</h1>

    <div class="cards">
      <div class="card" @click="$router.push('/timesheet/master')">
        <h3>Timesheet Master</h3>
      </div>

      <div class="card" @click="$router.push('/timesheet/reports')">
        <h3>Timesheet Reports</h3>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  padding: 40px;
  text-align: center;
}

.cards {
  display: flex;
  justify-content: center;
  gap: 40px;
  margin-top: 30px;
}

.card {
  padding: 40px;
  width: 220px;
  border: 1px solid #ccc;
  cursor: pointer;
  border-radius: 10px;
  transition: 0.3s;
}

.card:hover {
  background: #f5f5f5;
}
</style>