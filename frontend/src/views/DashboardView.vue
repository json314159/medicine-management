<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { medicineApi } from '@/api'
import type { Dashboard } from '@/types'
const dashboard = ref<Dashboard | null>(null); const loading = ref(true); const error = ref('')
const formatCurrency = (value: number) => new Intl.NumberFormat('zh-CN', { style: 'currency', currency: 'CNY' }).format(value)
const formatDate = (date?: string) => date ? new Date(date).toLocaleDateString('zh-CN') : '—'
const typeLabel: Record<string, string> = { IN: '入库', OUT: '出库', ADJUST: '盘点调整' }
async function load() { loading.value = true; error.value = ''; try { dashboard.value = await medicineApi.dashboard() } catch (e) { error.value = (e as Error).message } finally { loading.value = false } }
onMounted(load)
</script>
<template>
  <section class="page"><div class="page-heading"><div><p class="eyebrow">DASHBOARD</p><h1>库存工作台</h1><p>及时掌握药品库存与有效期风险</p></div><button class="button secondary" @click="load">刷新数据</button></div>
    <div v-if="error" class="alert error">{{ error }}</div><div v-else-if="loading" class="loading">正在载入数据…</div>
    <template v-else-if="dashboard"><div class="stat-grid"><article class="stat-card blue"><span>药品品种</span><strong>{{ dashboard.medicineCount }}</strong><small>已建立档案</small></article><article class="stat-card green"><span>当前库存</span><strong>{{ dashboard.totalStock }}</strong><small>全部库存数量</small></article><article class="stat-card violet"><span>库存金额</span><strong>{{ formatCurrency(dashboard.inventoryValue) }}</strong><small>按采购价统计</small></article><article class="stat-card orange"><span>风险预警</span><strong>{{ dashboard.lowStockCount + dashboard.expirySoonCount }}</strong><small>低库存 {{ dashboard.lowStockCount }} · 近效期 {{ dashboard.expirySoonCount }}</small></article></div>
      <div class="dashboard-grid"><article class="panel"><div class="panel-title"><h2>低库存预警</h2><span class="badge warning">{{ dashboard.lowStockCount }} 项</span></div><div v-if="!dashboard.lowStockMedicines.length" class="empty">暂无低库存药品</div><table v-else><thead><tr><th>药品</th><th>当前库存</th><th>预警值</th></tr></thead><tbody><tr v-for="item in dashboard.lowStockMedicines" :key="item.id"><td><strong>{{ item.name }}</strong><small>{{ item.code }}</small></td><td class="danger-text">{{ item.stockQuantity }} {{ item.unit }}</td><td>{{ item.stockThreshold }} {{ item.unit }}</td></tr></tbody></table></article>
        <article class="panel"><div class="panel-title"><h2>近效期预警</h2><span class="badge danger">{{ dashboard.expirySoonCount }} 项</span></div><div v-if="!dashboard.expirySoonMedicines.length" class="empty">暂无近效期药品</div><table v-else><thead><tr><th>药品</th><th>批号</th><th>有效期</th></tr></thead><tbody><tr v-for="item in dashboard.expirySoonMedicines" :key="item.id"><td><strong>{{ item.name }}</strong><small>{{ item.code }}</small></td><td>{{ item.batchNo || '—' }}</td><td class="danger-text">{{ formatDate(item.expiryDate) }}</td></tr></tbody></table></article></div>
      <article class="panel transactions"><div class="panel-title"><h2>最近库存流水</h2></div><div v-if="!dashboard.recentTransactions.length" class="empty">尚无库存操作记录</div><table v-else><thead><tr><th>时间</th><th>药品</th><th>类型</th><th>数量</th><th>库存变化</th><th>备注</th></tr></thead><tbody><tr v-for="item in dashboard.recentTransactions" :key="item.id"><td>{{ new Date(item.createdAt).toLocaleString('zh-CN') }}</td><td><strong>{{ item.medicineName }}</strong><small>{{ item.medicineCode }}</small></td><td><span class="badge" :class="item.type.toLowerCase()">{{ typeLabel[item.type] }}</span></td><td>{{ item.quantity }}</td><td>{{ item.beforeQuantity }} → {{ item.afterQuantity }}</td><td>{{ item.remark || '—' }}</td></tr></tbody></table></article>
    </template>
  </section>
</template>
