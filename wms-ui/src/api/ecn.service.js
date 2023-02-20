import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/masterdata`

const getList = async (parameter) => {
  const url = `/ecn/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).post(url, parameter)
  return data
}

const getOne = async (id) => {
  const url = `/ecn/${id}`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

const edit = async (id, options) => {
  // const url = `/nmd/${id}`
  const url = `/ecn`
  // const { data } = await createAuthInstance(baseUrl).put(url, options)
  const { data } = await createAuthInstance(baseUrl).put(url, { ...options, id })
  return data
}

const add = async (options) => {
  const url = '/ecn'
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}

const destroy = async (id) => {
  const url = `/ecn/${id}`
  const { data } = await createAuthInstance(baseUrl).delete(url)
  return data
}
const upload = async (formdata) => {
  const url = `/ecn/import`
  const { data } = await createAuthInstance(baseUrl).post(url, formdata)
  return data
}

const uploadBatchUpdate = async (formdata) => {
  const url = `/ecn/saveBatch`
  const { data } = await createAuthInstance(baseUrl).post(url, formdata)
  return data
}

export const ecnService = {
  getList,
  getOne,
  edit,
  add,
  destroy,
  upload,
  uploadBatchUpdate
}
