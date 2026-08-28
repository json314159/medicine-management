<script setup lang="ts">
import { computed, reactive, watch } from 'vue'
import type { Medicine, MedicinePayload } from '@/types'
const props = defineProps<{ medicine?: Medicine | null; submitting?: boolean }>()
const emit = defineEmits<{ save: [payload: MedicinePayload]; cancel: [] }>()
const blank = (): MedicinePayload => ({ code: '', name: '', genericName: '', specification: '', manufacturer: '', category: '', unit: '盒', purchasePrice: 0, salePrice: 0, stockQuantity: 0, stockThreshold: 0, expiryDate: '', batchNo: '', enabled: true })
const form = reactive<MedicinePayload>(blank())
const title = computed(() => props.medicine ? '编辑药品' : '新增药品')
watch(() => props.medicine, value => Object.assign(form, value ? { ...value, expiryDate: value.expiryDate ?? '' } : blank()), { immediate: true })
function submit() {
  if (!form.code.trim() || !form.name.trim()) return alert('请填写药品编码和药品名称')
  emit('save', { ...form, code: form.code.trim(), name: form.name.trim(), expiryDate: form.expiryDate || undefined })
}
</script>
<template>
  <div class="modal-backdrop">
    <form class="modal-card" @submit.prevent="submit">
      <header><h2>{{ title }}</h2><button type="button" class="icon-button" @click="emit('cancel')">×</button></header>
      <div class="form-grid">
        <label>药品编码 <input v-model="form.code" required placeholder="如 AMOX-001" /></label><label>药品名称 <input v-model="form.name" required placeholder="请输入药品名称" /></label>
        <label>通用名 <input v-model="form.genericName" /></label><label>药品分类 <input v-model="form.category" placeholder="如 抗感染药" /></label>
        <label>规格 <input v-model="form.specification" placeholder="如 0.25g×24粒" /></label><label>单位 <input v-model="form.unit" required /></label>
        <label>生产厂家 <input v-model="form.manufacturer" /></label><label>批号 <input v-model="form.batchNo" /></label>
        <label>采购单价（元）<input v-model.number="form.purchasePrice" type="number" min="0" step="0.01" required /></label><label>销售单价（元）<input v-model.number="form.salePrice" type="number" min="0" step="0.01" required /></label>
        <label>初始库存 <input v-model.number="form.stockQuantity" type="number" min="0" required /></label><label>预警库存 <input v-model.number="form.stockThreshold" type="number" min="0" required /></label>
        <label>有效期 <input v-model="form.expiryDate" type="date" /></label><label class="check-label"><input v-model="form.enabled" type="checkbox" /> 启用此药品</label>
      </div>
      <footer><button type="button" class="button secondary" @click="emit('cancel')">取消</button><button class="button primary" :disabled="submitting">{{ submitting ? '保存中…' : '保存' }}</button></footer>
    </form>
  </div>
</template>
