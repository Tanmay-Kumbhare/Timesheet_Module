<template>

<HeaderBar />

<div class="app-layout">

  <SideBar />

  <main class="page-content">
    <router-view />
  </main>

</div>

</template>

<script setup>

import HeaderBar from "@/layout/HeaderBar.vue"
import SideBar from "@/layout/SideBar.vue"

</script>

<style>

.app-layout{
display:flex;
}

.page-content{
flex:1;
padding:20px;
}

</style>