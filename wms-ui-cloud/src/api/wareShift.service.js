import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/binin`

const getList = async (parameter) => {
  const url = `/ware-shift/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

const addWareShift = async (options) => {
  const url = '/ware-shift/add'
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}

const getReturnMaterialList = async (parameter) => {
  const url = `/returnMaterial/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

const cancel = async (options) => {
  const url = `/ware-shift/cancel/${options.id}`
  const { data } = await createAuthInstance(baseUrl).put(url)
  return data
}

const generateWareShiftByCall = async (options) => {
  const url = `/ware-shift/generateWareShiftByCall`
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}

const exportExcel = async (options) => {
  const url = `/ware-shift/exportExcel`
  const { data } = await createAuthInstance(baseUrl).post(url, options, {
    responseType: 'blob',
    headers: { 'Content-Type': 'application/json' }
  })
  return data
}


const exportReturnExcel = async (options) => {
  const url = `/returnMaterial/export`
  const { data } = await createAuthInstance(baseUrl).post(url, options, {
    responseType: 'blob',
    headers: { 'Content-Type': 'application/json' }
  })
  return data
}

export const wareShiftService = {
  getList,
  addWareShift,
  getReturnMaterialList,
  cancel,
  generateWareShiftByCall,
  exportExcel,
  exportReturnExcel
}
