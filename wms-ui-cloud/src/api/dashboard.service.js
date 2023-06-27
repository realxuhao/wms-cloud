import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/master-data`

const getBinInSummary = async (parameter) => {
  const url = `/report/bin`
  const { data } = await createAuthInstance(baseUrl).post(url, parameter)
  return data 
}

const getMissionToDoSummary = async (parameter) => {
  const url = `/report/getMissionToDo`
  const { data } = await createAuthInstance(baseUrl).post(url, parameter)
  return data 
}

const getOldMaterialSummary = async (parameter) => {
  const url = `/report/oldMaterial`
  const { data } = await createAuthInstance(baseUrl).post(url, parameter)
  return data 
}

const exportOldMaterial = async (parameter) => {
  const url = `/report/oldMaterialExport`
  const { data } = await createAuthInstance(baseUrl).post(url, parameter,{
    responseType:'blob'
  })
  return data 
}



export const dashboardService = {
  getBinInSummary,
  getMissionToDoSummary,
  getOldMaterialSummary,
  exportOldMaterial
}
