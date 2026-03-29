<template>

<h2>Master Data - Timesheet</h2>

<table>

<tr>
<td>Task Type</td>
<td>
<router-link to="/timesheet/master/task-types">
Open Page
</router-link>
</td>
</tr>

<tr>
<td>Task Master</td>
<td>
<router-link to="/timesheet/master/task-master">
Open Page
</router-link>
</td>
</tr>

<tr>
<td>Notification Settings</td>
<td>
<router-link to="/timesheet/master/notifications">
Open Page
</router-link>
</td>
</tr>

</table>

<router-view />

</template>