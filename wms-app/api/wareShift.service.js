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


export const wareShiftService = {
  getList,
  binDown,
  getWaitingBinIn,
  allocateBin,
  getReturnMaterialList
}