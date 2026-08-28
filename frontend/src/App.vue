<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { RouterLink, RouterView, useRoute } from 'vue-router'
import LoginView from '@/views/LoginView.vue'
import { authApi } from '@/api'
const route = useRoute()
const navItems = [{ to: '/', label: '工作台', icon: '▦' }, { to: '/medicines', label: '药品管理', icon: '▣' }]
const authenticated=ref(false); const username=computed(()=>sessionStorage.getItem('medicine-user')||'')
async function signedIn(){ authenticated.value=true } async function logout(){ try{await authApi.logout()}finally{ sessionStorage.clear(); authenticated.value=false } } onMounted(async()=>{ sessionStorage.removeItem('medicine-auth'); try{const user=await authApi.me(); sessionStorage.setItem('medicine-role',user.role);sessionStorage.setItem('medicine-user',user.username);authenticated.value=true}catch{sessionStorage.clear()} })
</script>
<template>
  <LoginView v-if="!authenticated" @login="signedIn" />
  <div v-else class="app-shell">
    <aside class="sidebar"><div class="brand"><span class="brand-mark">✚</span><span>药品管理</span></div><nav><RouterLink v-for="item in navItems" :key="item.to" :to="item.to" class="nav-item" :class="{ active: route.path === item.to }"><span>{{ item.icon }}</span>{{ item.label }}</RouterLink></nav><div class="sidebar-footer">{{ username }}<br /><button class="logout-button" @click="logout">退出登录</button></div></aside>
    <main class="content"><RouterView /></main>
  </div>
</template>
