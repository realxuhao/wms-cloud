import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/masterdata`

const getList = async (parameter) => {
  const url = `/ware/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

const getOne = async (id) => {
  const url = `/ware/${id}`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

const edit = async (id, options) => {
  const url = `/ware`
  const { data } = await createAuthInstance(baseUrl).put(url, { ...options, id })
  return data
}

const add = async (options) => {
  const url = '/ware'
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}

const destroy = async (id) => {
  const url = `/ware/${id}`
  const { data } = await createAuthInstance(baseUrl).delete(url)
  return data
}

const getOptionList = async () => {
  const url = `/ware/all/list`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

export const wareService = {
  getList,
  getOne,
  edit,
  add,
  destroy,
  getOptionList
}
