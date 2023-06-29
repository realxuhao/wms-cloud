import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/product`

const planImport = async (formdata) => {
  const url = `/productPackaging/planImport`
  const { data } = await createAuthInstance(baseUrl).post(url, formdata)
  return data
}

const getAllPlanList = async (parameter) => {
  const url = `/productPackaging/allPlanList?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

const destroyPlan = async (ids) => {
  const url = `/productPackaging/deletePlan/${ids}`
  const { data } = await createAuthInstance(baseUrl).delete(url)
  return data
}
const deleteTask = async (ids) => {
  const url = `/productPackaging/deleteTask/${ids}`
  const { data } = await createAuthInstance(baseUrl).delete(url)
  return data
}
const completeTask = async (ids) => {
  const url = `/productPackaging/complete/${ids}`
  const { data } = await createAuthInstance(baseUrl).put(url)
  return data
}
const genTask = async (options) => {
  const url = `/productPackaging/genTask?${qs.stringify(options)}`
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}

// const getTaskList = async (options) => {
//   const url = `/productPackaging/list`
//   const { data } = await createAuthInstance(baseUrl).post(url, options)
//   return data
// }
const getTaskList = async (parameter) => {
  const url = `/productPackaging/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}
const getDashboard = async (parameter) => {
  const url = `/productPackaging/getDashboard?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}
const getHistoryRecord = async (parameter) => {
  const url = `/productPackaging/getHistoryRecord?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}
const uploadBatchUpdate = async (formdata) => {
  const url = `/productPackaging/saveBatch`
  const { data } = await createAuthInstance(baseUrl).post(url, formdata)
  return data
}

const importSPDN = async (formdata) => {
  const url = `/spdn/importSPDN`
  const { data } = await createAuthInstance(baseUrl).post(url, formdata)
  return data
}

const spdnList = async (parameter) => {
  const url = `/spdn/spdnList?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

const approveSpdnList = async (parameter) => {
  const url = `/spdn/approve/${parameter}`
  const { data } = await createAuthInstance(baseUrl).put(url)
  return data
}

const deleteSpdn = async (parameter) => {
  const url = `/spdn/${parameter}`
  const { data } = await createAuthInstance(baseUrl).delete(url)
  return data
}

const spdnStocklist = async (parameter) => {
  const url = `/spdn/spdnStocklist?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

const SUQAIQCManagementList = async (parameter) => {
  const url = `/SUQAIQCManagement/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

const changeSUQAStatus = async (parameter) => {
  const url = `/SUQAIQCManagement/changeSUQAStatus`
  const { data } = await createAuthInstance(baseUrl).post(url,parameter)
  return data
}

const validateSUQAStatus = async (parameter) => {
  const url = `/SUQAIQCManagement/validateSUQAStatus`
  const { data } = await createAuthInstance(baseUrl).post(url,parameter)
  return data
}


const spdnPicklist = async (parameter) => {
  const url = `/spdn/spdnPicklist?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}


export const finishedProductService = {
  planImport,
  genTask,
  getTaskList,
  getAllPlanList,
  getHistoryRecord,
  destroyPlan,
  getDashboard,
  completeTask,
  deleteTask,
  uploadBatchUpdate,
  importSPDN,
  spdnList,
  approveSpdnList,
  spdnStocklist,
  deleteSpdn,
  SUQAIQCManagementList,
  changeSUQAStatus,
  validateSUQAStatus,
  spdnPicklist
}
