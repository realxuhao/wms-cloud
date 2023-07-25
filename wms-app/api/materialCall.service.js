import request from './general'

const waitingBinDownList = async (data) => {
	const url = `/binin/materialKanban/waitingBinDownList`
	const method = 'GET'

	return request({
		url,
		method,
		data
	})
}

const binDownList = async (data) => {
	const url = '/binin/materialKanban/binDownList'
	const method = 'GET'
	return request({
		url,
		method,
		data
	})
}

const binDown = async (ssccNumber) => {
	const url = `/binin/materialKanban/binDown/${ssccNumber}`
	const method = 'PUT'

	return request({
		url,
		method,
	})
}

const confirmMaterial = async (data) => {
	const url = `/binin/materialKanban/confirmMaterial`
	const method = 'GET'

	return request({
		url,
		method,
		data: data
	})
}

const getBarCodeInfo = async (barCode) => {
	const url = `/binin/materialKanban/getKanbanBySSCCAndStatus/?mesBarCode=${barCode}`
	const method = 'GET'

	return request({
		url,
		method,
	})
}


export const materialCallService = {
	waitingBinDownList,
	binDown,
	binDownList,
	confirmMaterial,
	getBarCodeInfo
}