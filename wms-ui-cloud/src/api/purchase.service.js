import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/vehiclereservation`

const getList = async (parameter) => {
  const url = `/purchase/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

const getListBySupplierName = async (name, parameter) => {
  const url = `/purchase/list/${name}?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

const close = async (id) => {
  const url = `/purchase/close/${id}`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

const detail = async (id) => {
  const url = `/purchase/detail/${id}`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

const syncdata = async () => {
  const url = `/purchase/syncdata`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

export const purchaseService = {
  getList,
  getListBySupplierName,
  close,
  detail,
  syncdata
}
