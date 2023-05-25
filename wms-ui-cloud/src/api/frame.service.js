import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/masterdata`

const getList = async (parameter) => {
  const url = `/frame/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

const getOne = async (id) => {
  const url = `/frame/${id}`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

const edit = async (id, options) => {
  const url = `/frame`
  const { data } = await createAuthInstance(baseUrl).put(url, { ...options, id })
  return data
}

const add = async (options) => {
  const url = '/frame'
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}

const destroy = async (id) => {
  const url = `/frame/${id}`
  const { data } = await createAuthInstance(baseUrl).delete(url)
  return data
}

const upload = async (formdata) => {
  const url = `/frame/import`
  const { data } = await createAuthInstance(baseUrl).post(url, formdata)
  return data
}

const uploadBatchUpdate = async (formdata) => {
  const url = `/frame/saveBatch`
  const { data } = await createAuthInstance(baseUrl).post(url, formdata)
  return data
}

const getFrameTypeList = async (parameter) => {
  const url = `/frame/getFrameType?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

export const frameService = {
  getList,
  getOne,
  edit,
  add,
  destroy,
  upload,
  uploadBatchUpdate,
  getFrameTypeList
}
