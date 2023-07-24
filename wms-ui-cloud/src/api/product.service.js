import config from '@/config/api.config'
import { createAuthInstance } from './general'
 import qs from 'qs'

const baseUrl = `${config.apiHost}/masterdata`

const getList = async (parameter) => {
  const url = `/productFrame/list/getList?${qs.stringify(parameter)}`
  const { data } = await createAuthInstance(baseUrl).get(url, parameter)
  return data
}
const getReceivIngList = async (options) => {
  const url = `/productFrame/receivinglist`
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}
const getProductList = async (options) => {
  const url = `/mdProductPackaging/list`
  const { data } = await createAuthInstance(baseUrl).post(url, options)
  return data
}

const getOne = async (id) => {
  const url = `/productFrame/${id}`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

const edit = async (id, options) => {
  const url = `/productFrame/${id}`
  const { data } = await createAuthInstance(baseUrl).put(url, options)
  return data
}

const add = async (options) => {
  const { data } = await createAuthInstance(baseUrl).post('/productFrame/addMaterial', options)
  return data
}

const destroy = async (id) => {
  const url = `/productFrame/${id}`
  const { data } = await createAuthInstance(baseUrl).delete(url)
  return data
}

const getDispatchFrameTypeList = async (parameter) => {
  const url = `/productFrame/list`
  const { data } = await createAuthInstance(baseUrl).post(url, parameter)
  return data
}

const getDispatchRuleOne = async (id) => {
  const url = `/productFrame/${id}`
  const { data } = await createAuthInstance(baseUrl).get(url)
  return data
}

const addDispatchBin = async (options) => {
  const { data } = await createAuthInstance(baseUrl).post('/productFrame', options)
  return data
}
const editDispatchRule = async (id, options) => {
  const url = `/productFrame/${id}`
  const { data } = await createAuthInstance(baseUrl).put(url, options)
  return data
}

const destroyDispatchBin = async (id) => {
  const url = `/productFrame/${id}`
  const { data } = await createAuthInstance(baseUrl).delete(url)
  return data
}

const upload = async (formdata) => {
  const url = `/productFrame/import`
  const { data } = await createAuthInstance(baseUrl).post(url, formdata)
  return data
}

const uploadBatchUpdate = async (formdata) => {
  const url = `/productFrame/saveBatch`
  const { data } = await createAuthInstance(baseUrl).post(url, formdata)
  return data
}

const uploadMaterialBin = async (formdata) => {
  const url = `/productFrame/import`
  const { data } = await createAuthInstance(baseUrl).post(url, formdata)
  return data
}

const uploadMaterialBinBatchUpdate = async (formdata) => {
  const url = `/productFrame/saveBatch`
  const { data } = await createAuthInstance(baseUrl).post(url, formdata)
  return data
}

export const productService = {
  getList,
  getReceivIngList,
  getOne,
  edit,
  add,
  destroy,
  getProductList,
  getDispatchFrameTypeList,
  getDispatchRuleOne,
  addDispatchBin,
  editDispatchRule,
  destroyDispatchBin,
  upload,
  uploadBatchUpdate,
  uploadMaterialBin,
  uploadMaterialBinBatchUpdate
}
