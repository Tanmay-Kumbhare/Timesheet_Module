<template>

<h2>Timesheet Reports</h2>

<div class="reports">

<router-link to="/timesheet/reports/departmentwise">
Departmentwise Report
</router-link>

<router-link to="/timesheet/reports/institutewise">
Institutewise Report
</router-link>

<router-link to="/timesheet/reports/logs">
Notification Logs
</router-link>

</div>

<router-view />

</template>