import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/vehiclereservation`

const getList = async (parameter) => {
  const url = `/driverDeliver/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

const getOnceSupplierOnTime = async (parameter) => {
  const url = `/driverDeliver/supplierOnTime?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

const exportOnTime = async (options) => {
  const url = `/driverDeliver/exportOnTime`
  const { data } = await createAuthInstance(baseUrl).post(url, options, {
    responseType: 'blob',
    headers: { 'Content-Type': 'application/json' }
  })
  return data
}

const destroy = async (id) => {
  const url = `/driverDeliver/${id}`
  const { data } = await createAuthInstance(baseUrl).delete(url)
  return data
}

export const driverDeliverService = {
  getList,
  getOnceSupplierOnTime,
  exportOnTime,
  destroy
}
