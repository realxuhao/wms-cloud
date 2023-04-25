import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/product`

const getList = async (parameter) => {
  console.log(parameter)
  const url = `/stock-take-detail/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}
const confirm = async (options) => {
  const url = '/stock-take-detail/confirm'
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}
const editTakeQuantity = async (options) => {
  const url = '/stock-take-detail/editTakeQuantity'
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}
const issue = async (options) => {
  const url = '/stock-take-detail/issue'
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}

export const stockTakeDetailService = {
  getList,
  confirm,
  editTakeQuantity,
  issue
}
