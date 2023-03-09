import request from './general'

const getList = async (data) => {
  const url = `/binin/ware-shift/list`
  const method = 'GET'

  return request({
    url,
    method,
    data
  })
}

const binDown = async (barCode) => {
  const url = `/binin/ware-shift/binDown/${barCode}`
  const method = 'PUT'

  return request({
    url,
    method,
  })
}


const getWaitingBinIn = async (data) => {
  const url = `/binin/ware-shift/getWaitingBinIn`
  const method = 'GET'

  return request({
    url,
    method,
	data
  })
}

const allocateBin = async (barCode) => {
  const url = `/binin/ware-shift/allocateBin/${barCode}`
  const method = 'GET'

  return request({
    url,
    method,
  })
}

const getReturnMaterialList = async (data) => {
  const url = `/binin/returnMaterial/list`
  const method = 'POST'

  return request({
    url,
    method,
	data
  })
}

const addMaterialReturn = async (data) => {
  const url = `/binin/returnMaterial/addMaterialReturn`
  const method = 'POST'

  return request({
    url,
    method,
	data
  })
}

const getCellList = async (data) => {
  const url = `/masterdata/department/list`
  const method = 'GET'

  return request({
    url,
    method,
	data
  })
}

const getWareList = async (data) => {
  const url = `/masterdata/area/areaVOList`
  const method = 'GET'

  return request({
    url,
    method,
	data
  })
}

const returnMaterialConfirm = async (ssccNumbers) => {
  const url = `/binin/returnMaterial/confirm/${ssccNumbers}`
  const method = 'PUT'

  return request({
    url,
    method,
  })
}

const getAllocateBin = async (mesBarcode) => {
  const url = `/binin/returnMaterial/allocateBin/${mesBarcode}`
  const method = 'GET'

  return request({
    url,
    method,
  })
}

const postBinIn = async (data) => {
  const url = `/binin/returnMaterial/in`
  const method = 'POST'

  return request({
    url,
    method,
	data
  })
}


const shiftBinIn = async (data) => {
  const url = `/binin/ware-shift/binIn`
  const method = 'POST'

  return request({
    url,
    method,
	data
  })
}

export const wareShiftService = {
  getList,
  binDown,
  getWaitingBinIn,
  allocateBin,
  getReturnMaterialList,
  addMaterialReturn,
  getCellList,
  getWareList,
  returnMaterialConfirm,
  getAllocateBin,
  postBinIn,
  shiftBinIn
}