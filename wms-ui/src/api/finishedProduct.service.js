import config from '@/config/api.config'
import { createAuthInstance } from './general'

const baseUrl = `${config.apiHost}/product`

const planImport = async (formdata) => {
  const url = `/productPackaging/planImport`
  const { data } = await createAuthInstance(baseUrl).post(url, formdata)
  return data
}

const getAllPlanList = async (formdata) => {
  const url = `/productPackaging/allPlanList`
  const { data } = await createAuthInstance(baseUrl).post(url, formdata)
  return data
}

const destroyPlan = async (ids) => {
  const url = `/productPackaging/destroyPlan/${ids}`
  const { data } = await createAuthInstance(baseUrl).delete(url)
  return data
}

const genTask = async (options) => {
  const url = `/productPackaging/genTask`
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}

const getTaskList = async (options) => {
  const url = `/productPackaging/list`
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}

const getHistoryRecord = async (options) => {
  const url = `/productPackaging/getHistoryRecord`
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}

export const finishedProductService = {
  planImport,
  genTask,
  getTaskList,
  getAllPlanList,
  getHistoryRecord,
  destroyPlan
}
