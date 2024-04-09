import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/binin`

const getList = async (parameter) => {
  const url = `/stock/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

const exportExcel = async (options) => {
  const url = `/stock/exportExcel`
  const { data } = await createAuthInstance(baseUrl).post(url, options, {
    responseType: 'blob',
    headers: { 'Content-Type': 'application/json' }
  })
  return data
}

const getAdjustList = async (parameter) => {
  const url = `/stock/adjustList?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

const exportStockAdjust = async (options) => {
  const url = `/stock/exportStockAdjust`
  const { data } = await createAuthInstance(baseUrl).post(url, options, {
    responseType: 'blob',
    headers: { 'Content-Type': 'application/json' }
  })
  return data
}

export const stockService = {
  getList,
  exportExcel,
  getAdjustList,
  exportStockAdjust
}
