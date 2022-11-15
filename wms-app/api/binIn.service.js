

import request from './general'

const getPendingBinInList = async (data) => {
  const url = `/binin/bin-in/list`
  const method = 'GET'

  return request({
    url,
    method,
    data
  })
}

const getBinInHistoryList = async (data) => {
  const url = `/binin/bin-in/currentUserData`
  const method = 'GET'

  return request({
    url,
    method,
    data
  })
}

const getByMesBarCode = async (barCode) =>{
	const url = `/binin/bin-in/getByMesBarCode/${barCode}`
	const method = 'GET'
	
	return request({
	  url,
	  method
	})
}

const postMaterialIn  = async (data) => {
  const url = `/binin/material-in/check`
  const method = 'POST'

  return request({
    url,
    method,
    data
  })
}

const getPalletList = async (parameter) =>{
  const url = `/masterdata/pallet/list`
  const method = 'GET'
  
  return request({
	url,
	method,
	data:parameter
  })
}

const getPalletTypeCode = async (palletType) =>{
	const url = `/binin/bin-in/virtualPalletCode/${palletType}`
	const method = 'GET'
	
	return request({
		url,
		method,
	})
}


const allocate = async (data) =>{
	const url = '/binin/bin-in/allocate'
	const method = 'POST'
	
	return request({
		url,
		method,
		data
	})
}

const generateInTask = async (data) =>{
	const url = '/binin/bin-in/generateInTask'
	const method = 'POST'
	
	return request({
		url,
		method,
		data
	})
}

const postBinIn = async (data) =>{
	const url = '/binin/bin-in/in'
	const method = 'POST'
	
	return request({
		url,
		method,
		data
	})
}



export const binInService = {
  getPendingBinInList,
  getBinInHistoryList,
  getByMesBarCode,
  postMaterialIn,
  getPalletList,
  getPalletTypeCode,
  allocate,
  generateInTask,
  postBinIn
}
