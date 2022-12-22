import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/binin`

const getList = async (parameter) => {
  const url = `/manual-transfer/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}
const batchIssueJob = async (options) => {
  const url = `/manual-transfer/issue/${options.ids.join(',')}`
  const { data } = await createAuthInstance(baseUrl).put(url, options)
  return data
}
const cancelManualTrans = async (options) => {
  const url = `/manual-transfer/cancel/${options.ssccNb}`
  const { data } = await createAuthInstance(baseUrl).put(url)
  return data
}
export const manualTransService = {
  getList,
  batchIssueJob,
  cancelManualTrans
}
