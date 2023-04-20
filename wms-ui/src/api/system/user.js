import config from '@/config/api.config'
import { createAuthInstance } from '../general'
import qs from 'qs'

const baseUrl = `${config.apiHost}`

// 查询用户列表
export const listUser = async (parameter) => {
  const url = `/system/user/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

// 查询用户详细
export const getUser = async (userId) => {
  const url = `/system/user/` + userId
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

// 新增用户
export const addUser = async (options) => {
  const url = '/system/user'
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}
// 修改用户
export const updateUser = async (options) => {
  const url = '/system/user'
  const { data } = await createAuthInstance(baseUrl).put(url, options)
  return data
}

// 删除用户
export const delUser = async (userId) => {
  const url = `/system/user/` + userId
  const { data } = await createAuthInstance(baseUrl).delete(url)
  return data
}

// 用户密码重置
export const resetUserPwd = async (userId, password) => {
  const options = {
    userId,
    password
  }
  const url = '/system/user/resetPwd'
  const { data } = await createAuthInstance(baseUrl).put(url, options)
  return data
}

// 用户状态修改
export const changeUserStatus = async (userId, status) => {
  const options = {
    userId,
    status
  }
  const url = '/system/user/changeStatus'
  const { data } = await createAuthInstance(baseUrl).put(url, options)
  return data
}

// 查询用户个人信息
export const getUserProfile = async () => {
  const url = `/system/user/profile`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

// 修改用户个人信息
export const updateUserProfile = async (options) => {
  const url = '/system/user/profile'
  const { data } = await createAuthInstance(baseUrl).put(url, options)
  return data
}

// 用户密码重置
export const updateUserPwd = async (oldPassword, newPassword) => {
  const options = {
    oldPassword,
    newPassword
  }
  const url = '/system/user/profile/updatePwd'
  const { data } = await createAuthInstance(baseUrl).put(url, options)
  return data
}

// 用户头像上传
export const uploadAvatar = async (options) => {
  const url = '/system/user/profile/avatar'
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}

// 导入用户
export const importData = async (options) => {
  const url = '/system/user/importData'
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}

// 查询授权角色
export const getAuthRole = async (userId) => {
  const url = `/system/user/authRole/` + userId
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

// 保存授权角色
export const updateAuthRole = async (options) => {
  const url = '/system/user/authRole'
  const { data } = await createAuthInstance(baseUrl).put(url, options)
  return data
}

// 查询部门下拉树结构
export const deptTreeSelect = async () => {
  const url = `/system/user/deptTree`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}
