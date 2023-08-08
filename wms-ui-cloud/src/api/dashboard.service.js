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
const getProMissionToDoSummary = async (parameter) => {
  const url = `/report/getMissionToDoPro`
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

const exportWorkload = async (options) => {
  const url = `/report/workloadExport`
  const { data } = await createAuthInstance(baseUrl).post(url, options, {
    responseType: 'blob',
    headers: { 'Content-Type': 'application/json' }
  })
  return data
}

const getWorkloadPro = async (parameter) => {
  const url = `/report/workloadPro?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).post(url, parameter)
  return data
}

const exportWorkloadPro = async (options) => {
  const url = `/report/workloadExportPro`
  const { data } = await createAuthInstance(baseUrl).post(url, options, {
    responseType: 'blob',
    headers: { 'Content-Type': 'application/json' }
  })
  return data
}

const processEfficiency = async (parameter) => {
  const url = `/report/processEfficiency?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).post(url, parameter)
  return data
}

const proInOutStock = async (parameter) => {
  const url = `/report/proInOutStock?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).post(url, parameter)
  return data
}

const proInOutStockExport = async (options) => {
  const url = `/report/proInOutStockExport?${qs.stringify(options)}`
  const { data } = await createAuthInstance(baseUrl).post(url, options, {
    responseType: 'blob',
    headers: { 'Content-Type': 'application/json' }
  })
  return data
}

export const dashboardService = {
  getBinInSummary,
  getMissionToDoSummary,
  getProMissionToDoSummary,
  getOldMaterialSummary,
  exportOldMaterial,
  getExpiredMaterial,
  exportExpiredMaterial,
  getWareShiftList,
  getWorkload,
  exportWorkload,
  getWorkloadPro,
  exportWorkloadPro,
  processEfficiency,
  proInOutStock,
  proInOutStockExport
}
