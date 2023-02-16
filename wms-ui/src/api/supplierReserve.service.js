import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/vehiclereservation`

const getList = async (parameter) => {
  const url = `/supplierReserve/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

/** 根据条件(WareId&reserveDate)查询时间窗口列表 */
const getListByParam = async (parameter) => {
  const url = `/supplierReserve/timewindow?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

const addList = async (options) => {
  const url = '/supplierReserve/add'
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}

export const supplierReserveService = {
  getList,
  addList,
  getListByParam
}