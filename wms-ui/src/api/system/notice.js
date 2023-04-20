import config from '@/config/api.config'
import { createAuthInstance } from '../general'
import qs from 'qs'

const baseUrl = `${config.apiHost}`

// 查询公告列表
export const listNotice = async (parameter) => {
  const url = `/system/notice/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}
// 查询公告详细
export const getNotice = async (noticeId) => {
  const url = `/system/notice/` + noticeId
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

// 新增公告
export const addNotice = async (options) => {
  const url = '/system/notice'
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}

// 修改公告
export const updateNotice = async (options) => {
  const url = '/system/notice'
  const { data } = await createAuthInstance(baseUrl).put(url, options)
  return data
}

// 删除公告
export const delNotice = async (noticeId) => {
  const url = `/system/notice/` + noticeId
  const { data } = await createAuthInstance(baseUrl).delete(url)
  return data
}
