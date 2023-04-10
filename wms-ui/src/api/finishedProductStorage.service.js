import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/product`

const getStorageList = async (parameter) => {
  const url = `/product-receive/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

const deleteStorage = async (ids) => {
  const url = `/product-receive/${ids}`
  const { data } = await createAuthInstance(baseUrl).delete(url)
  return data
}

const storageImport = async (formdata) => {
  const url = `/product-receive/import`
  const { data } = await createAuthInstance(baseUrl).post(url, formdata)
  return data
}

const uploadBatchUpdate = async (formdata) => {
  const url = `/product-receive/saveBatch`
  const { data } = await createAuthInstance(baseUrl).post(url, formdata)
  return data
}

export const finishedProductStorageService = {
  getStorageList,
  deleteStorage,
  storageImport,
  uploadBatchUpdate
}
