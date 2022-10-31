import request from './general'

const getPendingMaterialList = async (data) => {
  const url = `/storagein/material-receive/list`
  const method = 'GET'

  return request({
    url,
    method,
    data
  })
}

const getMaterialInHistoryList = async (data) => {
  const url = `/storagein/material-in/currentUserData`
  const method = 'GET'

  return request({
    url,
    method,
    data
  })
}

const getAndCheckMaterialIn = async (ssccCode) =>{
	const url = `/storagein/material-in/getCheckByBarCode/${ssccCode}`
	const method = 'GET'
	
	return request({
	  url,
	  method
	})
}

const postMaterialIn  = async (data) => {
  const url = `/storagein/material-in/check`
  const method = 'POST'

  return request({
    url,
    method,
    data
  })
}

export const materialInService = {
  getPendingMaterialList,
  getMaterialInHistoryList,
  getAndCheckMaterialIn,
  postMaterialIn
}
