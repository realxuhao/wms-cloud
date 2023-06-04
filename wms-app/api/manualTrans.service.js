import request from './general'

const getList = async (data) => {
	const url = `/binin/manual-transfer/list`
	const method = 'GET'

	return request({
		url,
		method,
		data
	})
}

const binDown = async (barCode) => {
	const url = `/binin/manual-transfer/binDown/${barCode}`
	const method = 'PUT'

	return request({
		url,
		method,
	})
}

const info = async (barCode) => {
	const url = `/binin/manual-transfer/info/${barCode}`
	const method = 'GET'

	return request({
		url,
		method,
	})
}

const postBinIn = async (data) => {
	const url = '/binin/manual-transfer/in'
	const method = 'POST'

	return request({
		url,
		method,
		data
	})
}


// const getWaitingBinIn = async (data) => {
//   const url = `/binin/ware-shift/getWaitingBinIn`
//   const method = 'GET'

//   return request({
//     url,
//     method,
// 	data
//   })
// }

const allocateBin = async (barCode) => {
	const url = `/binin/manual-transfer/allocateBin/${barCode}`
	const method = 'GET'

	return request({
		url,
		method,
	})
}

const trans = async (options) => {
	const url = `/binin/manual-transfer/trans`
	const method = 'POST'

	return request({
		url,
		data: options,
		method,
	})
}



export const manualTransService = {
	getList,
	binDown,
	allocateBin,
	info,
	postBinIn,
	trans

}
