import config from '@/config/api.config'
import { createAuthInstance } from '../general'
import qs from 'qs'

const baseUrl = `${config.apiHost}`

// 查询菜单列表
export const listMenu = async (parameter) => {
  const url = `/system/menu/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

// 查询菜单详细
export const getMenu = async (menuId) => {
  const url = `/system/menu/` + menuId
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}
// 查询菜单下拉树结构
export const treeselect = async () => {
  const url = `/system/menu/treeselect`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

// 根据角色ID查询菜单下拉树结构
export const roleMenuTreeselect = async (roleId) => {
  const url = `/system/menu/roleMenuTreeselect/` + roleId
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

// 新增菜单
export const addMenu = async (options) => {
  const url = '/system/menu'
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}
// 修改菜单
export const updateMenu = async (options) => {
  const url = '/system/menu'
  const { data } = await createAuthInstance(baseUrl).put(url, options)
  return data
}

// 删除菜单
export const delMenu = async (menuId) => {
  const url = `/system/menu/` + menuId
  const { data } = await createAuthInstance(baseUrl).delete(url)
  return data
}
