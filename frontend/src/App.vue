<script setup lang="ts">
import { RouterLink, RouterView, useRoute } from 'vue-router'
const route = useRoute()
const navItems = [{ to: '/', label: '工作台', icon: '▦' }, { to: '/medicines', label: '药品管理', icon: '▣' }]
</script>
<template>
  <div class="app-shell">
    <aside class="sidebar"><div class="brand"><span class="brand-mark">✚</span><span>药品管理</span></div><nav><RouterLink v-for="item in navItems" :key="item.to" :to="item.to" class="nav-item" :class="{ active: route.path === item.to }"><span>{{ item.icon }}</span>{{ item.label }}</RouterLink></nav><div class="sidebar-footer">v1.0.0<br />库存管理平台</div></aside>
    <main class="content"><RouterView /></main>
  </div>
</template>
