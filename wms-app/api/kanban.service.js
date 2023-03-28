import request from './general'

const generateOrder = async ({
	codeList,
	carNb
}) => {
	const url = `/binin/materialKanban/genOrderAndSetStatus/${carNb}`
	const method = 'POST'

	return request({
		url,
		method,
		data: codeList
	})
}


const getTranshipmentOrder = async (data) => {
	const url = `/binin/materialKanban/getTranshipmentOrder`
	const method = 'GET'

	return request({
		url,
		method,
		data
	})
}

const confirmOrder = async (data) => {
	const url = `/binin/materialKanban/confirmOrder`
	const method = 'POST'

	return request({
		url,
		method,
		data: data.sscc
	})
}

// 根据barcode的sscc获取kanban数据 注：若返回600为移库任务
const checkKanbanTask = async (data) => {
	const url = `/binin/materialKanban/getKanbanBySSCC`
	const method = 'GET'

	return request({
		url,
		method,
		data
	})
}

// 移库任务上架分配库位
const allocateBin = async (mesBarcode) => {
	const url = `/binin/ware-shift/allocateBin/${mesBarcode}`
	const method = 'GET'

	return request({
		url,
		method,
	})
}

// 整托下架配送接口
const deliver = async (data) => {
	const url = `/binin/materialKanban/deliver`
	const method = 'GET'

	return request({
		url,
		method,
		data
	})
}

// 拆托
const splitPallet = async (data) => {
	const url = `/binin/materialKanban/splitPallet`
	const method = 'POST'

	return request({
		url,
		method,
		data
	})
}

const parsedBarCode = async (mesBarcode) => {
	const url = `/master-data/mesBarCode/parseMesBarCode/${mesBarcode}`
	const method = 'GET'

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



export const kanbanService = {
	generateOrder,
	getTranshipmentOrder,
	confirmOrder,
	checkKanbanTask,
	allocateBin,
	deliver,
	splitPallet,
	parsedBarCode,
	getWaitingBinIn
}
