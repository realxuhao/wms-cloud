import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/binin`

const getList = async (parameter) => {
  const url = `/material-feeding/call/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

const upload = async (formdata) => {
  const url = `/material-feeding/call`
  const { data } = await createAuthInstance(baseUrl).post(url, formdata)
  return data
}

const getDepartmentList = async () => {
  const url = `/masterdata/department/list`
  const { data } = await createAuthInstance(config.apiHost).get(url)
  return data
}

export const materialFeedingService = {
  getList,
  upload,
  getDepartmentList
}
