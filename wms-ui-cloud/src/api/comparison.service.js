import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/product`

const getList = async (parameter) => {
  const url = `/rm_comparison/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}
const getProList = async (parameter) => {
  const url = `/rm_comparison/proList?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

const upload = async (formdata) => {
  const url = `/rm_comparison/import`
  const { data } = await createAuthInstance(baseUrl).post(url, formdata)
  return data
}
const uploadPro = async (formdata) => {
  const url = `/rm_comparison/importPro`
  const { data } = await createAuthInstance(baseUrl).post(url, formdata)
  return data
}
const updateBySsccList = async (parameter) => {
  console.log(parameter)
  const url = `/rm_comparison/updateBySsccList`
  const { data } = await createAuthInstance(baseUrl).post(url, parameter.ssccList)
  return data
}

const updateByIdList = async (parameter) => {
  console.log(parameter)
  const url = `/rm_comparison/updateByIdList`
  const { data } = await createAuthInstance(baseUrl).post(url, parameter.ids)
  return data
}

export const comparisonService = {
  getList,
  upload,
  updateBySsccList,
  uploadPro,
  getProList,
  updateByIdList
}
