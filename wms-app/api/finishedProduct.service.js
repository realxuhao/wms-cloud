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

export const finishedProductService = {
	getTaskList,
	addPackageHistory,
	deleteMultiPackageHistory,
	deletePackageHistory,
	getHistoryRecord,
	getAllTaskList
}
