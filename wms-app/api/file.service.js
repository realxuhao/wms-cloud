import request from './general'

const getFileUrl = async (fileName) => {
	const url = `/wms-file/file/download`
	const method = 'POST'

	return request({
		url,
		method,
		data: {
			fileName
		}
	})
}


export const fileService = {
	getFileUrl
}
