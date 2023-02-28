import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/binin`

const getIqcQualityList = async (parameter) => {
  const url = `/IQCManagement/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

const updateIqcQuality = async (id, options) => {
  // const url = `/nmd/${id}`
  const url = `/IQCManagement/changeStatus`
  // const { data } = await createAuthInstance(baseUrl).put(url, options)
  const { data } = await createAuthInstance(baseUrl).post(url, { ...options, id })
  return data
}

export const iqcManagementService = {
  getIqcQualityList,
  updateIqcQuality
}
