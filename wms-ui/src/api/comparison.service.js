import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/product`

const getList = async (parameter) => {
  console.log(parameter)
  const url = `/rm_comparison/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}
const upload = async (formdata) => {
  const url = `/rm_comparison/import`
  const { data } = await createAuthInstance(baseUrl).post(url, formdata)
  return data
}

export const comparisonService = {
  getList,
  upload
}
