import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/master-data`

const getBinInSummary = async (parameter) => {
  const url = `/report/bin`
  const { data } = await createAuthInstance(baseUrl).post(url, parameter)
  return data
}

const getMissionToDoSummary = async (parameter) => {
  const url = `/report/getMissionToDo`
  const { data } = await createAuthInstance(baseUrl).post(url, parameter)
  return data
}

const getOldMaterialSummary = async (parameter) => {
  const url = `/report/oldMaterial?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).post(url, parameter)
  return data
}

const exportOldMaterial = async (parameter) => {
  const url = `/report/oldMaterialExport`
  const { data } = await createAuthInstance(baseUrl).post(url, parameter, {
    responseType: 'blob'
  })
  return data
}
const getExpiredMaterial = async (parameter) => {
  const url = `/report/expiredMaterial?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).post(url, parameter)
  return data
}

const exportExpiredMaterial = async (parameter) => {
  const url = `/report/expiredMaterialExport`
  const { data } = await createAuthInstance(baseUrl).post(url, parameter, {
    responseType: 'blob'
  })
  return data
}

const getWareShiftList = async (parameter) => {
  const url = `/report/wareShift?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).post(url, parameter)
  return data
}
const getWorkload = async (parameter) => {
  const url = `/report/workload?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).post(url, parameter)
  return data
}

const exportWorkload = async (parameter) => {
  const url = `/report/workloadExport`
  const { data } = await createAuthInstance(baseUrl).post(url, parameter, {
    responseType: 'blob'
  })
  return data
}
const processEfficiency = async (parameter) => {
  const url = `/report/processEfficiency?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).post(url, parameter)
  return data
}

export const dashboardService = {
  getBinInSummary,
  getMissionToDoSummary,
  getOldMaterialSummary,
  exportOldMaterial,
  getExpiredMaterial,
  exportExpiredMaterial,
  getWareShiftList,
  getWorkload,
  exportWorkload,
  processEfficiency

}
