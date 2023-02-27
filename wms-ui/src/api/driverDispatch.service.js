import config from '@/config/api.config'
import { createAuthInstance } from './general'

const baseUrl = `${config.apiHost}/vehiclereservation`

/** 获取今天签到车辆数据，包含以预约和未预约现场签到的 */
const getTodaySignlist = async (options) => {
  const url = `/driverDispatch/signlist`
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}

/** 获取今天已预约未签到车辆数据 */
const getTodayNoSignList = async () => {
  const url = `/driverDispatch/nosignlist`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

/** 分配道口 */
const importDock = async (options) => {
  const url = `/driverDispatch/dock`
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}

/** 进厂 */
const enter = async (id) => {
  const url = `/driverDispatch/enter/${id}`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

/** 完成 */
const complete = async (id) => {
  const url = `/driverDispatch/complete/${id}`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

export const driverDispatchService = {
  getTodaySignlist,
  getTodayNoSignList,
  importDock,
  enter,
  complete
}
