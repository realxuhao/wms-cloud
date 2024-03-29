import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/binin`

const getList = async (parameter) => {
  const url = `/bin-in/list?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}

const destory = async (id) => {
  const url = `/bin-in/${id}`
  const { data } = await createAuthInstance(baseUrl).delete(url)
  return data
}

export const binInService = {
  getList,
  delete: destory
}
