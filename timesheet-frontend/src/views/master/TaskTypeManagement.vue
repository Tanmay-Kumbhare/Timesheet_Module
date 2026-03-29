<template>
  <div class="task-type-management">
    <h2>Task Type Management</h2>

    <!-- Add/Edit Form -->
    <div class="form-section">
      <input v-model="form.name" placeholder="Task Type Name" />
      <label><input type="checkbox" v-model="form.isActive" /> Active</label>
      <button @click="save">{{ editingId ? 'Update' : 'Add' }}</button>
      <button v-if="editingId" @click="cancelEdit">Cancel</button>
      <span v-if="formError" class="error">{{ formError }}</span>
    </div>

    <!-- Table -->
    <div v-if="loading">Loading...</div>
    <table v-else>
      <thead>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Active</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="type in taskTypes" :key="type.id">
          <td>{{ type.id }}</td>
          <td>{{ type.name }}</td>
          <td>{{ type.isActive ? 'Yes' : 'No' }}</td>
          <td>
            <button @click="edit(type)">Edit</button>
            <button @click="remove(type.id)">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>

import {ref,onMounted} from "vue"
import {masterApi} from "@/api"

const taskTypes=ref([])
const form=ref({name:"",isActive:true})
const editId=ref(null)

async function load(){

const res=await masterApi.list("taskTypes")
taskTypes.value=res.data

}

async function save(){

if(editId.value){

await masterApi.update(
"taskTypes",
editId.value,
form.value
)

}else{

await masterApi.create(
"taskTypes",
form.value
)

}

form.value={name:"",isActive:true}
editId.value=null

load()

}

async function remove(id){

await masterApi.delete("taskTypes",id)

load()

}

function edit(item){

editId.value=item.id
form.value={...item}

}

onMounted(load)

</script>

<style scoped>
.module-layout { display: flex; padding: 20px; gap: 20px; }
.crud-main { flex: 1; }
.page-title { text-align: center; margin-bottom: 16px; font-size: 17px; font-weight: bold; }
.form-card {
  border: 1px solid #ccc; border-radius: 4px; padding: 16px;
  margin-bottom: 20px; max-width: 480px; background: #fafafa;
}
.form-card h3 { margin-bottom: 12px; font-size: 15px; }
.form-row { display: flex; align-items: center; gap: 12px; margin-bottom: 12px; }
.form-row label { min-width: 80px; font-weight: 500; }
.form-row input { flex: 1; }
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