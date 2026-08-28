export interface ApiResponse<T> { success: boolean; message: string; data: T }
export interface CurrentUser { username: string; role: 'ADMIN' | 'OPERATOR' }
export interface Medicine {
  id: number; code: string; name: string; genericName?: string; specification?: string; manufacturer?: string; category?: string;
  unit: string; purchasePrice: number; salePrice: number; stockQuantity: number; stockThreshold: number; expiryDate?: string; batchNo?: string; enabled: boolean; updatedAt: string
}
export interface MedicinePayload {
  code: string; name: string; genericName?: string; specification?: string; manufacturer?: string; category?: string; unit: string;
  purchasePrice: number; salePrice: number; stockQuantity: number; stockThreshold: number; expiryDate?: string; batchNo?: string; enabled: boolean
}
export type TransactionType = 'IN' | 'OUT' | 'ADJUST'
export interface StockAdjustment { type: TransactionType; quantity: number; remark?: string }
export interface Transaction { id: number; medicineName: string; medicineCode: string; type: TransactionType; quantity: number; beforeQuantity: number; afterQuantity: number; remark: string; createdAt: string }
export interface Dashboard { medicineCount: number; totalStock: number; inventoryValue: number; lowStockCount: number; expirySoonCount: number; lowStockMedicines: Medicine[]; expirySoonMedicines: Medicine[]; recentTransactions: Transaction[] }
