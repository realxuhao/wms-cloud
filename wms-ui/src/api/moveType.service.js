import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/masterdata`

const getList = async (parameter) => {
  const url = `/moveType/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

const getOne = async (id) => {
  const url = `/moveType/${id}`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

const edit = async (id, options) => {
  const url = `/moveType/${id}`
  const { data } = await createAuthInstance(baseUrl).put(url, options)
  return data
}

const add = async (options) => {
  const url = '/moveType'
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}

const destroy = async (id) => {
  const url = `/moveType/${id}`
  const { data } = await createAuthInstance(baseUrl).delete(url)
  return data
}

export const moveTypeService = {
  getList,
  getOne,
  edit,
  add,
  destroy
}
