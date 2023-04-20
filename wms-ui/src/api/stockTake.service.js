import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/product`

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
export const stockTakeService = {
  getList,
  destroy
}
