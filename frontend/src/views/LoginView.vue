<script setup lang="ts">
import { ref } from 'vue'
import { authApi } from '@/api'
const emit = defineEmits<{ login: [] }>()
const username=ref('admin'), password=ref(''), error=ref(''), loading=ref(false)
async function login(){ loading.value=true; error.value=''; try{ const user=await authApi.login(username.value,password.value); sessionStorage.setItem('medicine-role',user.role); sessionStorage.setItem('medicine-user',user.username); emit('login') }catch(e){ error.value='账号或密码错误，请重新输入' }finally{loading.value=false} }
</script>
<template><main class="login-page"><form class="login-card" @submit.prevent="login"><span class="brand-mark">✚</span><h1>药品管理</h1><p>请输入账号和密码后访问系统</p><div v-if="error" class="alert error">{{ error }}</div><label>账号<input v-model.trim="username" autocomplete="username" required /></label><label>密码<input v-model="password" type="password" autocomplete="current-password" required /></label><button class="button primary" :disabled="loading">{{ loading?'登录中…':'登录' }}</button><small>管理员可维护药品；操作员可查看和办理库存。</small></form></main></template>
