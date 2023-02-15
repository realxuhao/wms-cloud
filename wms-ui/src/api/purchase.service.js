import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/vehiclereservation`

const getList = async (parameter) => {
  const url = `/purchase/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

const getListBySupplierName = async (name) => {
  const url = `/purchase/${name}`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

export const purchaseService = {
  getList,
  getListBySupplierName
}
