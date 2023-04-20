import config from '@/config/api.config'
import { createAuthInstance } from '../general'
import qs from 'qs'

const baseUrl = `${config.apiHost}`

// 查询岗位列表
export const listPost = async (parameter) => {
  const url = `/system/post/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

// 查询岗位详细
export const getPost = async (postId) => {
  const url = `/system/post/` + postId
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

// 新增岗位
export const addPost = async (options) => {
  const url = '/system/post'
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}

// 修改岗位
export const updatePost = async (options) => {
  const url = '/system/post'
  const { data } = await createAuthInstance(baseUrl).put(url, options)
  return data
}

// 删除岗位
export const delPost = async (postId) => {
  const url = `/system/post/` + postId
  const { data } = await createAuthInstance(baseUrl).delete(url)
  return data
}
