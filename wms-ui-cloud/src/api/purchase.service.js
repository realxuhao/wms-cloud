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

const getErrorPoCode = async () => {
  const url = `/purchase/getErrorPoCode`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

const getPoCodeList = async (parameter) => {
  const url = `/purchase/getPoCodeList?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

const getPoItemList = async (parameter) => {
  const url = `/purchase/getPoItemList?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

const getCmsNumberList = async (parameter) => {
  const url = `/purchase/getCmsNumberList?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

const getSupplierName = async () => {
  const url = `/purchase/getSupplierName`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

export const purchaseService = {
  getList,
  getListBySupplierName,
  close,
  detail,
  syncdata,
  getErrorPoCode,
  getPoCodeList,
  getPoItemList,
  getCmsNumberList,
  getSupplierName
}
