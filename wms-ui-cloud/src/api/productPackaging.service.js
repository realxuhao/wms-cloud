import config from '@/config/api.config'
import { createAuthInstance } from './general'
// import qs from 'qs'

const baseUrl = `${config.apiHost}/masterdata`

const getList = async (options) => {
  const url = `/mdProductPackaging/list`
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}
const uploadBatchUpdate = async (formdata) => {
  const url = `/mdProductPackaging/saveBatch`
  const { data } = await createAuthInstance(baseUrl).post(url, formdata)
  return data
}
const upload = async (formdata) => {
  const url = `/mdProductPackaging/import`
  const { data } = await createAuthInstance(baseUrl).post(url, formdata)
  return data
}
const getOne = async (id) => {
  const url = `/mdProductPackaging/${id}`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

const edit = async (id, options) => {
  const url = `/mdProductPackaging`
  const { data } = await createAuthInstance(baseUrl).put(url, { ...options, id })
  return data
}

const add = async (options) => {
  const { data } = await createAuthInstance(baseUrl).post('/mdProductPackaging', options)
  return data
}

const destroy = async (id) => {
  const url = `/mdProductPackaging/${id}`
  const { data } = await createAuthInstance(baseUrl).delete(url)
  return data
}
export const productPackagingService = {
  getList,
  uploadBatchUpdate,
  upload,
  getOne,
  edit,
  add,
  destroy
}
