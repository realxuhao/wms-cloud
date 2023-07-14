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

const postProductIn = async (options) => {
	const url = `/product/product-receive/receive/${options.barCode}`
	const method = 'PUT'

	return request({
		url,
		method,
		data: options
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

const getProductReceivingList = async (data) => {
	const url = `/product/product-shift/receivinglist`
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

const postBinIn = async (data) => {
	const url = '/product/product-shift/binIn'
	const method = 'POST'

	return request({
		url,
		method,
		data
	})
}

const mainReceiveConfirm = async (data) => {
	const url = `/product/product-shift/mainReceiveConfirm`
	const method = 'POST'

	return request({
		url,
		method,
		data: data.sscc
	})
}

const spdnPicklist = async (data) => {
	const url = `/product/spdn/spdnPicklist`
	const method = 'GET'

	return request({
		url,
		method,
		data
	})
}


const spdnBinDown = async (barCode) => {
	const url = `/product/spdn/binDown/${barCode}`
	const method = 'PUT'

	return request({
		url,
		method,
	})
}

const sudnBinDown = async (options) => {
	const url = `/product/sudn-pick/sumBinDown/${options.barCode}`
	const method = 'PUT'

	return request({
		url,
		method,
	})
}

const spdnGetByQrCode = async (barCode) => {
	const url = `/product/spdn/getByQrCode/${barCode}`
	const method = 'GET'

	return request({
		url,
		method,
	})
}

const spdnShip = async (data) => {
	const url = `/product/spdn/ship`
	const method = 'POST'

	return request({
		url,
		method,
		data
	})
}

const productStockGetByBarCode = async (barCode) => {
	const url = `/product/product-stock/getByBarCode/${barCode}`
	const method = 'GET'

	return request({
		url,
		method,
	})
}
const productStockEditStock = async (data) => {
	const url = `/product/product-stock/editStock`
	const method = 'POST'

	return request({
		url,
		method,
		data
	})
}

const productStockTrans = async (data) => {
	const method = 'POST'
	const url = `/product/product-stock/trans`
	return request({
		url,
		method,
		data
	})
}


const sudnList = async (data) => {
	const method = 'GET'
	const url = `/product/SUDN/getSUDNListByType`
	return request({
		url,
		method,
		data
	})
}

const sudnShipList = async (data) => {
	const method = 'GET'
	const url = `/product/SUDN/getSUDNShipListByType`
	return request({
		url,
		method,
		data
	})
}

const sudnPickList = async (data) => {
	const method = 'GET'
	const url = `/product/sudn-pick/binDownlist`
	return request({
		url,
		method,
		data
	})
}

const sudnPickGetByQrCode = async (options) => {
	console.log(options)
	const method = 'GET'
	const url = `/product/sudn-pick/getOneBinDown/${options.barCode}`
	return request({
		url,
		method,
	})
}

const sudnShipGetById = async (options) => {
	const method = 'GET'
	const url = `/product/SUDN/getById/${options.sudnId}`
	return request({
		url,
		method,
	})
}

const sudnShip = async (options) => {
	const method = 'POST'
	const url = `/product/SUDN/ship`
	return request({
		url,
		method,
		data: options
	})
}

const addProductReturn = async (options) => {
	const method = 'POST'
	const url = `/product/product-return/addProductReturn`
	return request({
		url,
		method,
		data: options
	})
}

const getOneBinDownBySSCC = async (sscc) => {
	const method = 'GET'
	const url = `/product/sudn-pick/getOneBinDownBySSCC/${sscc}`
	return request({
		url,
		method,
	})
}

const addSplit = async (options) => {
	const method = 'POST'
	const url = `/product/product-stock/addSplit`
	return request({
		url,
		method,
		data: options
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
	getProductReceivingList,
	productShiftGetBinInInfo,
	mainReceiveConfirm,
	postBinIn,
	spdnPicklist,
	spdnBinDown,
	spdnGetByQrCode,
	spdnShip,
	productStockGetByBarCode,
	productStockEditStock,
	productStockTrans,
	sudnList,
	sudnPickList,
	sudnPickGetByQrCode,
	sudnBinDown,
	sudnShipList,
	sudnShipGetById,
	sudnShip,
	addProductReturn,
	getOneBinDownBySSCC,
	addSplit
}