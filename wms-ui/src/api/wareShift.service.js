import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/binin`

const getList = async (parameter) => {
  const url = `/ware-shift/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

const addWareShift = async (options) => {
  const url = '/ware-shift/add'
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}
export const wareShiftService = {
  getList,
  addWareShift
}
