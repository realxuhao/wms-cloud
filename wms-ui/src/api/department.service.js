import config from '@/config/api.config'
import { createAuthInstance } from './general'

const baseUrl = `${config.apiHost}/masterdata`

const getList = async (parameter) => {
  const { data } = await createAuthInstance(baseUrl).get('/department/departmentVOList', parameter)
  return data
}

export const departmentService = {
  getList
}
