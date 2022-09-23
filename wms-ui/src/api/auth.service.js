import config from '@/config/api.config'
import { createAuthInstance, createInstance } from './general'

const baseUrl = `${config.apiHost}`

const login = async (parameter) => {
  const { data } = await createInstance(baseUrl).post('/auth/login', parameter)
  return data
}

const getCodeImg = async () => {
  const { data } = await createInstance(baseUrl).get('/code')
  return data
}

const getInfo = async () => {
  const path = '/system/user/getInfo'
  const { data } = await createAuthInstance(baseUrl).get(path)
  return data
}

export const authService = {
  login,
  getInfo,
  getCodeImg
}
