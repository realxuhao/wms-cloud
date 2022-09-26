import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/masterdata`

const getList = async (parameter) => {
  const url = `/timeWindow/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

const getOne = async (id) => {
  const url = `/timeWindow/${id}`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

const edit = async (id, options) => {
  const url = `/timeWindow/${id}`
  const { data } = await createAuthInstance(baseUrl).put(url, options)
  return data
}

const add = async (options) => {
  const url = '/timeWindow'
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}

const destroy = async (id) => {
  const url = `/timeWindow/${id}`
  const { data } = await createAuthInstance(baseUrl).delete(url)
  return data
}

export const timeWindowService = {
  getList,
  getOne,
  edit,
  add,
  destroy
}
