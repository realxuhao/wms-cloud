import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/storagein`

const getList = async (parameter) => {
  const url = `/material-in/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

const getReceiveList = async (parameter) => {
  const url = `/material-receive/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

const upload = async (formdata) => {
  const url = `/material-receive/import`
  const { data } = await createAuthInstance(baseUrl).post(url, formdata)
  return data
}

const uploadBatchUpdate = async (formdata) => {
  const url = `/material-receive/saveBatch`
  const { data } = await createAuthInstance(baseUrl).post(url, formdata)
  return data
}

export const materialInListService = {
  getList,
  getReceiveList,
  upload,
  uploadBatchUpdate
}
