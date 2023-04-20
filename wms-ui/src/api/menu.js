import config from '@/config/api.config'
import { createAuthInstance } from './general'

const baseUrl = `${config.apiHost}/`

// 获取路由
export const getRouters = async () => {
  const url = `/system/menu/getRouters`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}
