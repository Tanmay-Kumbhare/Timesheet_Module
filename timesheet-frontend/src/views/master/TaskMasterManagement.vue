<template>
  <div class="module-layout">
    <SidebarNav />
    <main class="crud-main">
      <h2 class="page-title">Task Master Management</h2>

      <div class="form-card">
        <h3>{{ editId ? 'Edit Task' : 'Add Task' }}</h3>
        <div class="form-row">
          <label>Name <span class="req">*</span></label>
          <input v-model="form.name" placeholder="Enter task name" />
        </div>
        <div class="form-row">
          <label>Task Type <span class="req">*</span></label>
          <select v-model="form.taskTypeId">
  <option value="">-- Select Task Type --</option>

  <option 
    v-for="type in taskTypes"
    :key="type.id"
    :value="type.id"
  >
    {{ type.name }}
  </option>

</select>
        </div>
        <div class="form-actions">
          <button class="btn-save" @click="save">{{ editId ? 'Update' : 'Save' }}</button>
          <button class="btn-cancel" @click="resetForm" v-if="editId">Cancel</button>
        </div>
        <p v-if="formError" class="error-msg">{{ formError }}</p>
      </div>

      <table>
        <thead>
          <tr>
            <th>Sr. No.</th>
            <th>Task Name</th>
            <th>Task Type</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="loading"><td colspan="5" class="center">Loading...</td></tr>
          <tr v-else-if="items.length === 0"><td colspan="5" class="center">No records found.</td></tr>
          <tr v-for="(item, idx) in items" :key="item.id">
            <td>{{ idx + 1 }}</td>
            <td>{{ item.name }}</td>
            <td>{{ item.taskType?.name }}</td>
            <td>
              <span :class="item.isActive ? 'badge-active' : 'badge-inactive'">
                {{ item.isActive ? 'Active' : 'Inactive' }}
              </span>
            </td>
            <td class="action-cell">
              <button class="btn-edit" @click="startEdit(item)">Edit</button>
              <button class="btn-delete" @click="remove(item.id)">Delete</button>
            </td>
          </tr>
        </tbody>
      </table>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import SidebarNav from '@/components/SidebarNav.vue'
import { masterApi} from '@/api/index.js'

const items     = ref([])
const taskTypes = ref([])
const loading   = ref(false)
const editId    = ref(null)
const formError = ref('')
const form      = ref({ name: '', taskTypeId: '' })

onMounted(async () => {

loading.value = true

try {

const [tasks, types] = await Promise.all([
  masterApi.list("taskMasters"),
  masterApi.list("taskTypes")
])

items.value = tasks.data
taskTypes.value = types.data

} finally {

loading.value = false

}

})

function startEdit(item) {
  editId.value = item.id
  form.value   = { name: item.name, taskTypeId: item.taskType?.id ?? '' }
  formError.value = ''
}

function resetForm() {
  editId.value = null
  form.value   = { name: '', taskTypeId: '' }
  formError.value = ''
}

async function loadTasks(){

const res = await masterApi.list("taskMasters")

items.value = res.data

}

async function save(){

await masterApi.create(
  "taskMasters",
  {
    name: form.value.name,
    taskType: { id: form.value.taskTypeId }
  }
)

form.value = {
  name: "",
  taskTypeId: ""
}

loadTasks()

}

async function remove(id){

if (!confirm("Delete this task?")) return

await masterApi.delete("taskMasters", id)

const res = await masterApi.list("taskMasters")

items.value = res.data

}
</script>

<style scoped>
.module-layout { display: flex; padding: 20px; gap: 20px; }
.crud-main { flex: 1; }
.page-title { text-align: center; margin-bottom: 16px; font-size: 17px; font-weight: bold; }
.form-card { border: 1px solid #ccc; border-radius: 4px; padding: 16px; margin-bottom: 20px; max-width: 480px; background: #fafafa; }
.form-card h3 { margin-bottom: 12px; font-size: 15px; }
.form-row { display: flex; align-items: center; gap: 12px; margin-bottom: 12px; }
.form-row label { min-width: 80px; font-weight: 500; }
.form-row input, .form-row select { flex: 1; }
.form-actions { display: flex; gap: 10px; }
.req { color: red; }
.error-msg { color: red; margin-top: 8px; font-size: 13px; }
.btn-save   { background: #4caf50; color: #fff; border: none; padding: 6px 18px; border-radius: 3px; }
.btn-cancel { background: #aaa;    color: #fff; border: none; padding: 6px 18px; border-radius: 3px; }
.btn-edit   { background: #2196f3; color: #fff; border: none; padding: 4px 12px; border-radius: 3px; margin-right: 6px; }
.btn-delete { background: #f44336; color: #fff; border: none; padding: 4px 12px; border-radius: 3px; }
.badge-active   { background: #e8f5e9; color: #388e3c; padding: 2px 8px; border-radius: 10px; font-size: 12px; }
.badge-inactive { background: #fce4ec; color: #c62828; padding: 2px 8px; border-radius: 10px; font-size: 12px; }
.action-cell { white-space: nowrap; }
.center { text-align: center; padding: 20px; color: #888; }
</style>