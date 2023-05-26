import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/product`

const add = async (options) => {
  const url = '/stock-take/add'
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}
const getList = async (parameter) => {
  console.log(parameter)
  const url = `/stock-take/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

const destroy = async (id) => {
  const url = `/stock-take/${id}`
  const { data } = await createAuthInstance(baseUrl).delete(url)
  return data
}


const exportList = async (options) => {
  const url = `/stock-take/export`
  const { data } = await createAuthInstance(baseUrl).post(url, options, {
    responseType: 'blob',
    headers: { 'Content-Type': 'application/json' }
  })
  return data
}

const exportDetailList = async (options) => {
  const url = `/stock-take-detail/export`
  const { data } = await createAuthInstance(baseUrl).post(url, options, {
    responseType: 'blob',
    headers: { 'Content-Type': 'application/json' }
  })
  return data
}


export const stockTakeService = {
  getList,
  destroy,
  add,
  exportList,
  exportDetailList
}
