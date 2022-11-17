import config from '@/config/api.config'
import { createAuthInstance } from './general'
import qs from 'qs'

const baseUrl = `${config.apiHost}/masterdata`

const getList = async (parameter) => {
  const url = `/material/materialVOList?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

const getOne = async (id) => {
  const url = `/material/${id}`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

const edit = async (id, options) => {
  const url = `/material/${id}`
  const { data } = await createAuthInstance(baseUrl).put(url, options)
  return data
}

const add = async (options) => {
  const { data } = await createAuthInstance(baseUrl).post('/material/addMaterial', options)
  return data
}

const destroy = async (id) => {
  const url = `/material/${id}`
  const { data } = await createAuthInstance(baseUrl).delete(url)
  return data
}

const getDispatchFrameTypeList = async (parameter) => {
  const url = `/materialBin/list`
  const { data } = await createAuthInstance(baseUrl).post(url, parameter)
  return data
}

const addDispatchBin = async (options) => {
  const { data } = await createAuthInstance(baseUrl).post('/materialBin', options)
  return data
}

const destroyDispatchBin = async (id) => {
  const url = `/materialBin/${id}`
  const { data } = await createAuthInstance(baseUrl).delete(url)
  return data
}

const upload = async (formdata) => {
  const url = `/material/import`
  const { data } = await createAuthInstance(baseUrl).post(url, formdata)
  return data
}

const uploadBatchUpdate = async (formdata) => {
  const url = `/material/saveBatch`
  const { data } = await createAuthInstance(baseUrl).post(url, formdata)
  return data
}

const uploadMaterialBin = async (formdata) => {
  const url = `/materialBin/import`
  const { data } = await createAuthInstance(baseUrl).post(url, formdata)
  return data
}

const uploadMaterialBinBatchUpdate = async (formdata) => {
  const url = `/materialBin/saveBatch`
  const { data } = await createAuthInstance(baseUrl).post(url, formdata)
  return data
}

export const materialService = {
  getList,
  getOne,
  edit,
  add,
  destroy,

  getDispatchFrameTypeList,
  addDispatchBin,
  destroyDispatchBin,
  upload,
  uploadBatchUpdate,
  uploadMaterialBin,
  uploadMaterialBinBatchUpdate
}
