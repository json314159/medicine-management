<script setup lang="ts">
import { reactive } from 'vue'
import type { Medicine, StockAdjustment } from '@/types'
const props = defineProps<{ medicine: Medicine; submitting?: boolean }>()
const emit = defineEmits<{ save: [payload: StockAdjustment]; cancel: [] }>()
const form = reactive<StockAdjustment>({ type: 'IN', quantity: 1, remark: '' })
function submit() {
  if (form.quantity < 1) return alert('数量必须大于 0')
  if (form.type === 'OUT' && form.quantity > props.medicine.stockQuantity) return alert('出库数量不能大于当前库存')
  emit('save', { ...form })
}
</script>
<template>
  <div class="modal-backdrop">
    <form class="modal-card narrow" @submit.prevent="submit"><header><h2>库存操作</h2><button type="button" class="icon-button" @click="emit('cancel')">×</button></header>
      <p class="muted"><strong>{{ medicine.name }}</strong> · 当前库存 {{ medicine.stockQuantity }} {{ medicine.unit }}</p>
      <div class="form-grid one-col"><label>操作类型 <select v-model="form.type"><option value="IN">入库</option><option value="OUT">出库</option><option value="ADJUST">盘点调整（设为数量）</option></select></label><label>{{ form.type === 'ADJUST' ? '调整后库存' : '操作数量' }} <input v-model.number="form.quantity" type="number" min="1" required /></label><label>备注 <textarea v-model="form.remark" rows="3" placeholder="可选" /></label></div>
      <footer><button type="button" class="button secondary" @click="emit('cancel')">取消</button><button class="button primary" :disabled="submitting">{{ submitting ? '提交中…' : '确认操作' }}</button></footer>
    </form>
  </div>
</template>
