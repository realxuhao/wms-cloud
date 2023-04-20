import config from '@/config/api.config'
import { createAuthInstance } from '../general'
import qs from 'qs'

const baseUrl = `${config.apiHost}`

// 查询角色列表
export const listRole = async (parameter) => {
  const url = `/system/role/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

// 查询角色详细
export const getRole = async (roleId) => {
  const url = `/system/role/` + roleId
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

// 新增角色
export const addRole = async (options) => {
  const url = '/system/role'
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}

// 修改角色
export const updateRole = async (options) => {
  const url = '/system/role'
  const { data } = await createAuthInstance(baseUrl).put(url, options)
  return data
}

// 角色数据权限
export const dataScope = async (options) => {
  const url = '/system/role/dataScope'
  const { data } = await createAuthInstance(baseUrl).put(url, options)
  return data
}

// 角色状态修改
export const changeRoleStatus = async (roleId, status) => {
  const options = {
    roleId,
    status
  }
  const url = '/system/role/changeStatus'
  const { data } = await createAuthInstance(baseUrl).put(url, options)
  return data
}

// 删除角色
export const delRole = async (roleId) => {
  const url = `/system/role/` + roleId
  const { data } = await createAuthInstance(baseUrl).delete(url)
  return data
}

// 查询角色已授权用户列表
export const allocatedUserList = async (parameter) => {
  const url = `/system/role/authUser/allocatedList?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

// 查询角色未授权用户列表
export const unallocatedUserList = async (parameter) => {
  const url = `/system/role/authUser/unallocatedList?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

// 取消用户授权角色
export const authUserCancel = async (options) => {
  const url = '/system/role/authUser/cancel'
  const { data } = await createAuthInstance(baseUrl).put(url, options)
  return data
}

// 批量取消用户授权角色
export const authUserCancelAll = async (options) => {
  const url = '/system/role/authUser/cancelAll'
  const { data } = await createAuthInstance(baseUrl).put(url, options)
  return data
}

// 授权用户选择
export const authUserSelectAll = async (options) => {
  const url = '/system/role/authUser/selectAll'
  const { data } = await createAuthInstance(baseUrl).put(url, options)
  return data
}

// 根据角色ID查询部门树结构
export const deptTreeSelect = async (roleId) => {
  const url = `/system/role/deptTree/` + roleId
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}
