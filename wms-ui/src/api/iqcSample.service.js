import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/binin/iqc`

const exportExcel = async (options) => {
  const url = `/sample/export`
  const { data } = await createAuthInstance(baseUrl).post(url, options, {
    responseType: 'blob',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
  })
  return data
}

const cancelIqc = async (options) => {
  const url = `/sample/cancel/${options.id}`
  const { data } = await createAuthInstance(baseUrl).put(url)
  return data
}

const addIqcManual = async (options) => {
  const url = '/sample/manualAdd'
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}

const modifySscc = async (formdata) => {
  const url = `/sample/modifySscc`
  const { data } = await createAuthInstance(baseUrl).post(url, formdata)
  return data
}

const getList = async (parameter) => {
  const url = `/sample/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

const getIqcQualityList = async (parameter) => {
  const url = `/sample/quality/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).post(url, parameter)
  return data
}

const updateIqcQuality = async (id, options) => {
  // const url = `/nmd/${id}`
  const url = `/sample/quality`
  // const { data } = await createAuthInstance(baseUrl).put(url, options)
  const { data } = await createAuthInstance(baseUrl).put(url, { ...options, id })
  return data
}

export const iqcSampleService = {
  exportExcel,
  cancelIqc,
  getList,
  addIqcManual,
  modifySscc,
  getIqcQualityList,
  updateIqcQuality
}
