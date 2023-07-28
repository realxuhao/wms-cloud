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

export const stockService = {
  getList,
  exportExcel
}
