import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/masterdata`

const getList = async (parameter) => {
  const url = `/fsmp/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).post(url, parameter)
  return data
}

const getOne = async (id) => {
  const url = `/fsmp/${id}`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

const edit = async (id, options) => {
  // const url = `/nmd/${id}`
  const url = `/fsmp`
  // const { data } = await createAuthInstance(baseUrl).put(url, options)
  const { data } = await createAuthInstance(baseUrl).put(url, { ...options, id })
  return data
}

const add = async (options) => {
  const url = '/fsmp'
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}

const destroy = async (id) => {
  const url = `/fsmp/${id}`
  const { data } = await createAuthInstance(baseUrl).delete(url)
  return data
}
const upload = async (formdata) => {
  const url = `/fsmp/import`
  const { data } = await createAuthInstance(baseUrl).post(url, formdata)
  return data
}

const uploadBatchUpdate = async (formdata) => {
  const url = `/fsmp/saveBatch`
  const { data } = await createAuthInstance(baseUrl).post(url, formdata)
  return data
}

export const fsmpService = {
  getList,
  getOne,
  edit,
  add,
  destroy,
  upload,
  uploadBatchUpdate
}
