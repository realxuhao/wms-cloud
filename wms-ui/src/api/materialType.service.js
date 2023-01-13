import config from '@/config/api.config'
import { createAuthInstance } from './general'

import qs from 'qs'

const baseUrl = `${config.apiHost}/masterdata`

const getList = async (parameter) => {
  const url = `/materialType/materialTypeVOList?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

const getByCell = async (cell) => {
  const url = `/materialType/getByCell/${cell}`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

const getOne = async (id) => {
  const url = `/materialType/${id}`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

const edit = async (id, options) => {
  const url = `/materialType`
  const { data } = await createAuthInstance(baseUrl).put(url, { id, ...options })
  return data
}

const add = async (options) => {
  const { data } = await createAuthInstance(baseUrl).post('/materialType', options)
  return data
}

const destroy = async (id) => {
  const url = `/materialType/${id}`
  const { data } = await createAuthInstance(baseUrl).delete(url)
  return data
}

export const materialTypeService = {
  getList,
  getOne,
  edit,
  add,
  destroy,
  getByCell
}
