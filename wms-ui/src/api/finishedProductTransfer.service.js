import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/product`

const getTransferList = async (parameter) => {
  const url = `/product-shift/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

const createTransfer = async (options) => {
  const url = `/product-shift/batchGenerateShift`
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}

const cancelTransfer = async (id) => {
  const url = `/product-shift/cancel/${id}`
  const { data } = await createAuthInstance(baseUrl).put(url)
  return data
}

export const finishedProductTransferService = {
  getTransferList,
  createTransfer,
  cancelTransfer
}
