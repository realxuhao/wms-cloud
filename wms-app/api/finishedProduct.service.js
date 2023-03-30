import request from './general'

const getTaskList = async (options) => {
	const url = `/product/productPackaging/getDashboard`
	const method = 'GET'

	return request({
		url,
		method,
		data: options
	})
}


export const finishedProductService = {
	getTaskList
}
