import config from '@/config/api.config'
import { createAuthInstance } from '../general'
import qs from 'qs'

const baseUrl = `${config.apiHost}`

// 查询部门列表
export const listDept = async (parameter) => {
  const url = `/system/dept/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}
// 查询部门列表（排除节点）
export const listDeptExcludeChild = async (deptId) => {
  const url = `/system/dept/list/exclude/` + deptId
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}
// 查询部门详细
export const getDept = async (deptId) => {
  const url = `/system/dept/` + deptId
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}
// 新增部门
export const addDept = async (options) => {
  const url = '/system/dept'
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}
// 修改部门
export const updateDept = async (options) => {
  const url = '/system/dept'
  const { data } = await createAuthInstance(baseUrl).put(url, options)
  return data
}
// 删除部门
export const delDept = async (deptId) => {
  const url = `/system/dept/` + deptId
  const { data } = await createAuthInstance(baseUrl).delete(url)
  return data
}
