import request from './general'

const getStockInfoByMesBarCode = async (mesBarcode) => {
	const url = `/bin-in/stock/getByMesBarCode/${mesBarcode}`
	const method = 'GET'

	return request({
		url,
		method,
	})
}

const getStockInfoBySscc = async (sscc) => {
	const url = `/bin-in/stock/getBySscc/${sscc}`
	const method = 'GET'

	return request({
		url,
		method,
	})
}

const getByBinCode = async (binCode) => {
	const url = `/bin-in/stock/getByBinCode/${binCode}`
	const method = 'GET'

	return request({
		url,
		method,
	})
}

const addSplit = async (options) => {
	const url = `/bin-in/split/add`
	const method = 'POST'

	return request({
		url,
		method,
		data: options
	})
}


// 盘点
const getStockTakeList = async (data) => {
	const url = `/product/stock-take-detail/taskList`
	const method = 'GET'

	return request({
		url,
		method,
		data
	})
}
const getStockTakeDetailList = async (data) => {
	const url = `/product/stock-take-detail/list`
	const method = 'GET'

	return request({
		url,
		method,
		data
	})
}

const getStockTakeDetail = async (barCode) => {
	const url = `/product/stock-take-detail/getByBarCode/${barCode}`
	const method = 'GET'

	return request({
		url,
		method,
	})
}

const operate = async (data) => {
	const url = `/product/stock-take-detail/operate`
	const method = 'POST'

	return request({
		url,
		method,
		data
	})
}


export const stockService = {
	getStockInfoByMesBarCode,
	getStockInfoBySscc,
	getByBinCode,
	addSplit,

	getStockTakeList,
	getStockTakeDetailList,
	getStockTakeDetail,
	operate
}
