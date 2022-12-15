import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/binin`

const getList = async (parameter) => {
  const url = `/material-feeding/call/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

const upload = async (formdata) => {
  const url = `/material-feeding/call`
  const { data } = await createAuthInstance(baseUrl).post(url, formdata)
  return data
}

const getDepartmentList = async () => {
  const url = `/masterdata/department/list`
  const { data } = await createAuthInstance(config.apiHost).get(url)
  return data
}

const getRuleList = async (parameter) => {
  const url = `/binin/stock/listByRule?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(config.apiHost).get(url)
  return data
}

const checkStock = async (options) => {
  const url = `binin/material-feeding/call/checkStock`
  const { data } = await createAuthInstance(config.apiHost).post(url, options)
  return data
}

// 人工拣配
const callPersonStock = async (options) => {
  const url = `binin/materialKanban/picking`
  const { data } = await createAuthInstance(config.apiHost).post(url, options)
  return data
}

// 系统拣配
const callSystemStock = async (options) => {
  const url = `binin/material-feeding/call/system`
  const { data } = await createAuthInstance(config.apiHost).post(url, options)
  return data
}

const updateQuantity = async (options) => {
  const url = `binin/material-feeding/call/${options.id}`
  const { data } = await createAuthInstance(config.apiHost).put(url, options)
  return data
}

const getPickingOrderList = async (options) => {
  const url = `binin/materialKanban/list`
  const { data } = await createAuthInstance(config.apiHost).post(url, options)
  return data
}

const batchAddJob = async (options) => {
  const url = `binin/materialKanban/issueJob/${options.ids.join(',')}`
  const { data } = await createAuthInstance(config.apiHost).put(url, options)
  return data
}

const cancelPickingOrder = async (options) => {
  const url = `binin/materialKanban/cancel/${options.id}`
  const { data } = await createAuthInstance(config.apiHost).put(url)
  return data
}

const exportExcel = async (options) => {
  const url = `binin/material-feeding/export`
  const { data } = await createAuthInstance(config.apiHost).post(url, options, {
    responseType: 'blob',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
  })
  return data
}

const getStockInfo = async (parameter) => {
  const url = `binin/materialKanban/getStockInfo?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(config.apiHost).get(url)
  return data
}

const addShiftTask = async (options) => {
  const url = `binin/ware-shift/addShiftTask`
  const { data } = await createAuthInstance(config.apiHost).post(url, options)
  return data
}

const confirmMaterial = async (options) => {
  const url = `binin/materialKanban/confirmMaterialBySSCCs?ssccs=${options}`
  const { data } = await createAuthInstance(config.apiHost).get(url, options)
  return data
}

export const materialFeedingService = {
  getList,
  upload,
  getDepartmentList,
  getRuleList,
  checkStock,
  callSystemStock,
  callPersonStock,
  updateQuantity,
  getPickingOrderList,
  batchAddJob,
  cancelPickingOrder,
  exportExcel,
  getStockInfo,
  addShiftTask,
  confirmMaterial
}
