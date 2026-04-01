<template>
  <div id="app">
    <Header />
    <div class="main-layout">
      <Sidebar />
      <div class="content">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script>
import Header from './components/Header.vue'
import Sidebar from './components/Sidebar.vue'

export default {
  components: { Header, Sidebar }
}
</script>

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
.main-layout { display: flex; }
.content { flex: 1; padding: 20px; background: #f5f5f5; min-height: 100vh; }
</style>