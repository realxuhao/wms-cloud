import request from './general'

const getTaskList = async (options) => {
	const url = `/product/productPackaging/planList/${options.id}`
	const method = 'GET'

	return request({
		url,
		method,
		// data: options
	})
}
const getAllTaskList = async (options) => {
	const url = `/product/productPackaging/getDashboard`
	const method = 'GET'

	return request({
		url,
		method,
		data: options
	})
}
const addPackageHistory = async (options) => {
	const url = `/product/productPackaging/addPackageHistory`
	const method = 'POST'

	return request({
		url,
		method,
		data: options
	})
}
const deleteMultiPackageHistory = async (options) => {
	const url = `/product/productPackaging/deleteMultiPackageHistory/${options.id}`
	const method = 'DELETE'

	return request({
		url,
		method,
		// data: options
	})
}
const deletePackageHistory = async (options) => {
	const url = `/product/productPackaging/deletePackageHistory/${options.id}`
	const method = 'DELETE'

	return request({
		url,
		method,
		// data: options
	})
}
const getHistoryRecord = async (options) => {
	const url = `/product/productPackaging/getHistoryRecord`
	const method = 'GET'

	return request({
		url,
		method,
		data: options
	})
}

/**------------成品入库--------------**/
const getProductInList = async (data) => {
	const url = `/product/product-receive/list`
	const method = 'GET'

	return request({
		url,
		method,
		data
	})
}

const getOneProductIn = async (barCode) => {
	const url = `/product/product-receive/getOne/${barCode}`
	const method = 'GET'

	return request({
		url,
		method,
	})
}

const postProductIn = async (barCode) => {
	const url = `/product/product-receive/receive/${barCode}`
	const method = 'GET'

	return request({
		url,
		method,
	})
}

/**------------成品移库--------------**/
const getProductShiftList = async (data) => {
	const url = `/product/product-shift/list`
	const method = 'GET'

	return request({
		url,
		method,
		data
	})
}

const postShift = async (data) => {
	const url = `/product/product-shift/ship`
	const method = 'POST'

	return request({
		url,
		method,
		data
	})
}

const productShiftGetBinInInfo = async (barCode) => {
	const url = `/product/product-shift/getBinInInfo/${barCode}`
	const method = 'GET'

	return request({
		url,
		method,
	})
}



export const finishedProductService = {
	getTaskList,
	addPackageHistory,
	deleteMultiPackageHistory,
	deletePackageHistory,
	getHistoryRecord,
	getAllTaskList,
	getProductInList,
	postProductIn,
	getOneProductIn,
	getProductShiftList,
	postShift,
	productShiftGetBinInInfo
}
