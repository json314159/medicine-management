<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { medicineApi } from '@/api'
import MedicineForm from '@/components/MedicineForm.vue'
import StockDialog from '@/components/StockDialog.vue'
import type { Medicine, MedicinePayload, StockAdjustment } from '@/types'
const medicines = ref<Medicine[]>([]); const keyword = ref(''); const lowStockOnly = ref(false); const loading = ref(false); const submitting = ref(false); const error = ref(''); const showForm = ref(false); const editing = ref<Medicine | null>(null); const stockMedicine = ref<Medicine | null>(null)
const filteredCount = computed(() => medicines.value.length)
const formatMoney = (value: number) => `¥${Number(value).toFixed(2)}`
const formatDate = (date?: string) => date ? new Date(date).toLocaleDateString('zh-CN') : '—'
async function load() { loading.value = true; error.value = ''; try { medicines.value = await medicineApi.list(keyword.value, lowStockOnly.value) } catch (e) { error.value = (e as Error).message } finally { loading.value = false } }
function openCreate() { editing.value = null; showForm.value = true }
function openEdit(medicine: Medicine) { editing.value = medicine; showForm.value = true }
async function save(payload: MedicinePayload) { submitting.value = true; try { editing.value ? await medicineApi.update(editing.value.id, payload) : await medicineApi.create(payload); showForm.value = false; await load() } catch (e) { alert((e as Error).message) } finally { submitting.value = false } }
async function adjust(payload: StockAdjustment) { if (!stockMedicine.value) return; submitting.value = true; try { await medicineApi.adjust(stockMedicine.value.id, payload); stockMedicine.value = null; await load() } catch (e) { alert((e as Error).message) } finally { submitting.value = false } }
async function remove(medicine: Medicine) { if (!confirm(`确定删除“${medicine.name}”吗？`)) return; try { await medicineApi.remove(medicine.id); await load() } catch (e) { alert((e as Error).message) } }
onMounted(load)
</script>
<template>
  <section class="page"><div class="page-heading"><div><p class="eyebrow">MEDICINES</p><h1>药品管理</h1><p>维护药品档案，执行入库、出库及库存盘点</p></div><button class="button primary" @click="openCreate">＋ 新增药品</button></div>
    <div class="toolbar"><input v-model="keyword" placeholder="搜索药品名称或编码" @keyup.enter="load" /><label class="toggle"><input v-model="lowStockOnly" type="checkbox" @change="load" /> 只看低库存</label><button class="button secondary" @click="load">查询</button><span class="result-count">共 {{ filteredCount }} 项</span></div>
    <div v-if="error" class="alert error">{{ error }}</div><div v-else-if="loading" class="loading">正在加载药品…</div><article v-else class="panel table-panel"><div v-if="!medicines.length" class="empty">暂无符合条件的药品，点击“新增药品”开始维护。</div><div v-else class="table-scroll"><table><thead><tr><th>药品信息</th><th>分类 / 厂家</th><th>批号 / 有效期</th><th>采购价</th><th>库存</th><th>状态</th><th>操作</th></tr></thead><tbody><tr v-for="item in medicines" :key="item.id"><td><strong>{{ item.name }}</strong><small>{{ item.code }}<template v-if="item.specification"> · {{ item.specification }}</template></small></td><td>{{ item.category || '—' }}<small>{{ item.manufacturer || '—' }}</small></td><td>{{ item.batchNo || '—' }}<small :class="{ 'danger-text': item.expiryDate && new Date(item.expiryDate).getTime() < Date.now() + 30 * 86400000 }">{{ formatDate(item.expiryDate) }}</small></td><td>{{ formatMoney(item.purchasePrice) }}</td><td><strong :class="{ 'danger-text': item.stockQuantity <= item.stockThreshold }">{{ item.stockQuantity }} {{ item.unit }}</strong><small>预警 {{ item.stockThreshold }}</small></td><td><span class="badge" :class="item.enabled ? 'enabled' : 'disabled'">{{ item.enabled ? '启用' : '停用' }}</span></td><td><div class="actions"><button class="link-button" @click="stockMedicine = item">库存</button><button class="link-button" @click="openEdit(item)">编辑</button><button class="link-button danger" @click="remove(item)">删除</button></div></td></tr></tbody></table></div></article>
    <MedicineForm v-if="showForm" :medicine="editing" :submitting="submitting" @save="save" @cancel="showForm = false" /><StockDialog v-if="stockMedicine" :medicine="stockMedicine" :submitting="submitting" @save="adjust" @cancel="stockMedicine = null" />
  </section>
</template>
