import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/binin`

const getIqcQualityList = async (parameter) => {
  const url = `/IQCManagement/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

const updateIqcQuality = async (options) => {
  // const url = `/nmd/${id}`
  const url = `/IQCManagement/changeStatus`
  // const { data } = await createAuthInstance(baseUrl).put(url, options)
  const { data } = await createAuthInstance(baseUrl).post(url, { ...options })
  return data
}

const uploadEditSSCCStatusFile = async (formdata) => {
  const url = `/IQCManagement/import`
  const { data } = await createAuthInstance(baseUrl).post(url, formdata)
  return data
}

const saveEditSSCCStatusList = async (options) => {
  const url = `/IQCManagement/excelChangeStatus`
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}

const modifyQuantity = async (options) => {
  const url = `/iqc/sample/modifyQuantity?${qs.stringify(options)}`
  const { data } = await createAuthInstance(baseUrl).put(url)
  return data
}

const issueJob = async (ids) => {
  const url = `/iqc/issueJob/${ids}`
  const { data } = await createAuthInstance(baseUrl).put(url)
  return data
}

export const iqcManagementService = {
  getIqcQualityList,
  updateIqcQuality,
  uploadEditSSCCStatusFile,
  saveEditSSCCStatusList,
  modifyQuantity,
  issueJob
}
