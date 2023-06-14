import request from './general'

const getList = async (data) => {
  const url = `/binin/split/list`
  const method = 'GET'

  return request({
    url,
    method,
    data
  })
}

const getInfo = async (barCode) => {
  const url = `/binin/split/getInfo/${barCode}`
  const method = 'GET'

  return request({
    url,
    method,
  })
}
const getBinInInfo = async (barCode) => {
  const url = `/binin/split/allocateBin/${barCode}`
  const method = 'GET'

  return request({
    url,
    method,
  })
}

const postBinIn = async (data) => {
  const url = '/binin/split/binIn'
  const method = 'POST'

  return request({
    url,
    method,
    data
  })
}

const splitPallet = async (data) => {
	const url = `/binin/split/add`
	const method = 'POST'

	return request({
		url,
		method,
		data
	})
}


export const splitPalletService = {
  getList,
  getInfo,
  getBinInInfo,
  postBinIn
}