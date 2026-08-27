import axios from 'axios'
import type { ApiResponse, Dashboard, Medicine, MedicinePayload, StockAdjustment } from './types'

const request = axios.create({ baseURL: '/api', timeout: 10000 })
request.interceptors.response.use(response => {
  const body = response.data as ApiResponse<unknown>
  if (!body.success) return Promise.reject(new Error(body.message))
  return response
}, error => Promise.reject(new Error(error.response?.data?.message ?? '网络请求失败，请确认后端服务已启动')))
const data = <T>(promise: Promise<{ data: ApiResponse<T> }>) => promise.then(response => response.data.data)
export const medicineApi = {
  list: (keyword?: string, lowStock?: boolean) => data<Medicine[]>(request.get('/medicines', { params: { keyword, lowStock: lowStock || undefined } })),
  create: (payload: MedicinePayload) => data<Medicine>(request.post('/medicines', payload)),
  update: (id: number, payload: MedicinePayload) => data<Medicine>(request.put(`/medicines/${id}`, payload)),
  remove: (id: number) => data<void>(request.delete(`/medicines/${id}`)),
  adjust: (id: number, payload: StockAdjustment) => data<Medicine>(request.post(`/medicines/${id}/stock`, payload)),
  dashboard: () => data<Dashboard>(request.get('/dashboard'))
}
