import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/product`

const getQualityList = async (parameter) => {
  const url = `/IQCManagement/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

const updateIqcQuality = async (options) => {
  const url = `/IQCManagement/changeStatus`
  const { data } = await createAuthInstance(baseUrl).post(url, { ...options })
  return data
}

const saveEditSSCCStatusList = async (options) => {
  const url = `/IQCManagement/excelChangeStatus`
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}

const validateStatus = async (options) => {
  const url = `/IQCManagement/validateStatus`
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}

export const finishedProductQualityManageService = {
  getQualityList,
  updateIqcQuality,
  saveEditSSCCStatusList,
  validateStatus
}
