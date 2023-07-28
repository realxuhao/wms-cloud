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

const innerReceivingList = async (data) => {
	const url = `/binin/ware-shift/innerReceivingList`
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
	const method = 'GET'

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

const returnMaterialConfirm = async (data) => {
	const url = `/binin/returnMaterial/confirm`
	const method = 'POST'

	return request({
		url,
		method,
		data,
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

const getOneBinDown = async (sscc) => {
	const url = `/binin/ware-shift/getOneBinDown/${sscc}`
	const method = 'GET'

	return request({
		url,
		method,
	})
}

const splitPallet = async (options) => {
	const url = `/binin/ware-shift/splitPallet`
	const method = 'POST'

	return request({
		url,
		method,
		data: options
	})
}

const getTransInfo = async (barCode) => {
	const url = `/binin/trans-ship/getTransInfo/${barCode}`
	const method = 'GET'

	return request({
		url,
		method,
	})
}

const getOne = async (barCode) => {
	const url = `/binin/ware-shift/getOne/${barCode}`
	const method = 'GET'

	return request({
		url,
		method,
	})
}

const batchBinIn = async (options) => {
	const url = `/binin/ware-shift/batchBinIn`
	const method = 'POST'

	return request({
		url,
		method,
		data: options
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
	shiftBinIn,
	innerReceivingList,
	getOneBinDown,
	splitPallet,
	getTransInfo,
	batchBinIn,
	getOne
}
