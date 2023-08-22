import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/product`

const getInventoryList = async (parameter) => {
  const url = `/product-stock/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

const productStockExport = async (options) => {
  const url = `/product-stock/export`
  const { data } = await createAuthInstance(baseUrl).post(url, options, {
    responseType: 'blob',
    headers: { 'Content-Type': 'application/json' }
  })
  return data
}

export const finishedProductInventoryService = {
  getInventoryList,
  productStockExport
}
