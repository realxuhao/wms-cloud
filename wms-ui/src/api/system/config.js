import config from '@/config/api.config'
import { createAuthInstance } from '../general'
import qs from 'qs'

const baseUrl = `${config.apiHost}`

// 查询参数列表
export const listConfig = async (parameter) => {
  const url = `/system/config/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}
// 查询参数详细
export const getConfig = async (configId) => {
  const url = `/system/config/` + configId
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}
// 根据参数键名查询参数值
export const getConfigKey = async (configKey) => {
  const url = `/system/config/configKey/` + configKey
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}
// 新增参数配置
export const addConfig = async (options) => {
  const url = '/system/config'
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}
// 修改参数配置
export const updateConfig = async (options) => {
  const url = '/system/config'
  const { data } = await createAuthInstance(baseUrl).put(url, options)
  return data
}
// 删除参数配置
export const delConfig = async (configId) => {
  const url = `/system/config/` + configId
  const { data } = await createAuthInstance(baseUrl).delete(url)
  return data
}
// 刷新参数缓存
export const refreshCache = async () => {
  const url = `/system/config/refreshCache`
  const { data } = await createAuthInstance(baseUrl).delete(url)
  return data
}
