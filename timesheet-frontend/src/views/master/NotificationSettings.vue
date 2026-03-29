<template>
  <div class="module-layout">
    <SidebarNav />
    <main class="crud-main">
      <h2 class="page-title">Notification Settings</h2>

      <div class="form-card">
        <h3>{{ editId ? 'Edit Setting' : 'Add Setting' }}</h3>
        <div class="form-row">
          <label>Type <span class="req">*</span></label>
          <select v-model="form.notificationType">
            <option value="">-- Select --</option>
            <option value="EMAIL_REMINDER">Email Reminder</option>
            <option value="APPROVAL_ALERT">Approval Alert</option>
          </select>
        </div>
        <div class="form-row">
          <label>Recipient <span class="req">*</span></label>
          <select v-model="form.recipientRole">
            <option value="">-- Select --</option>
            <option value="EMPLOYEE">Employee</option>
            <option value="HOD">HOD</option>
            <option value="COORDINATOR">Coordinator</option>
          </select>
        </div>
        <div class="form-row">
          <label>Subject <span class="req">*</span></label>
          <input v-model="form.subject" placeholder="Email subject" />
        </div>
        <div class="form-row align-top">
          <label>Message <span class="req">*</span></label>
          <textarea v-model="form.messageTemplate" rows="4"
            placeholder="Use {employee_name}, {period}, {date} as placeholders" />
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
            <th>Type</th>
            <th>Recipient</th>
            <th>Subject</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="loading"><td colspan="6" class="center">Loading...</td></tr>
          <tr v-else-if="items.length === 0"><td colspan="6" class="center">No records found.</td></tr>
          <tr v-for="(item, idx) in items" :key="item.id">
            <td>{{ idx + 1 }}</td>
            <td>{{ typeLabel(item.notificationType) }}</td>
            <td>{{ item.recipientRole }}</td>
            <td>{{ item.subject }}</td>
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
import { masterApi } from '@/api/index.js'

const items     = ref([])
const loading   = ref(false)
const editId    = ref(null)
const formError = ref('')
const form      = ref({ notificationType: '', recipientRole: '', subject: '', messageTemplate: '' })

const typeLabel = (t) => t === 'EMAIL_REMINDER' ? 'Email Reminder' : 'Approval Alert'

onMounted(load)

async function load() {
  loading.value = true
  try { items.value = await masterApi.list() }
  finally { loading.value = false }
}

function startEdit(item) {
  editId.value = item.id
  form.value = { ...item }
  formError.value = ''
}

function resetForm() {
  editId.value = null
  form.value = { notificationType: '', recipientRole: '', subject: '', messageTemplate: '' }
  formError.value = ''
}

async function save() {
  formError.value = ''
  if (!form.value.notificationType) { formError.value = 'Type is required.'; return }
  if (!form.value.recipientRole)    { formError.value = 'Recipient is required.'; return }
  if (!form.value.subject.trim())   { formError.value = 'Subject is required.'; return }
  if (!form.value.messageTemplate.trim()) { formError.value = 'Message is required.'; return }
  try {
    if (editId.value) {
      await masterApi.update(editId.value, form.value)
    } else {
      await masterApi.create(form.value)
    }
    resetForm()
    await load()
  } catch (e) { formError.value = e.message }
}

async function remove(id) {
  if (!confirm('Delete this notification setting?')) return
  await masterApi.delete(id)
  await load()
}
</script>

<style scoped>
.module-layout { display: flex; padding: 20px; gap: 20px; }
.crud-main { flex: 1; }
.page-title { text-align: center; margin-bottom: 16px; font-size: 17px; font-weight: bold; }
.form-card { border: 1px solid #ccc; border-radius: 4px; padding: 16px; margin-bottom: 20px; max-width: 540px; background: #fafafa; }
.form-card h3 { margin-bottom: 12px; font-size: 15px; }
.form-row { display: flex; align-items: center; gap: 12px; margin-bottom: 12px; }
.form-row.align-top { align-items: flex-start; }
.form-row label { min-width: 90px; font-weight: 500; }
.form-row input, .form-row select, .form-row textarea { flex: 1; }
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