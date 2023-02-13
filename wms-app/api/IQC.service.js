import request from './general'

const getList = async (data) => {
  const url = `/binin/iqc/binDown/list`
  const method = 'GET'

  return request({
    url,
    method,
    data
  })
}

const binDown = async (barCode) => {
  const url = `/binin/iqc/binDown/${barCode}`
  const method = 'PUT'

  return request({
    url,
    method,
  })
}

const getSample = async (barCode) => {
  const url = `/binin/iqc/sample/${barCode}`
  const method = 'GET'

  return request({
    url,
    method,
  })
}

const getSampleList = async (data) => {
  const url = `/binin/iqc/sample/list`
  const method = 'GET'

  return request({
    url,
    method,
    data
  })
}

const sampleConfirm = async (data) => {
  const url = `/binin/iqc/sample/confirm`
  const method = 'POST'

  return request({
    url,
    method,
    data
  })
}

const getBinInInfo = async (barCode) => {
  const url = `/binin/iqc/binIn/${barCode}`
  const method = 'GET'

  return request({
    url,
    method,
  })
}

const postBinIn = async (data) => {
  const url = '/binin/iqc/binIn'
  const method = 'POST'

  return request({
    url,
    method,
    data
  })
}


export const IQCService = {
  getList,
  binDown,
  getSample,
  
  getSampleList,
  sampleConfirm,
  
  postBinIn,
getBinInInfo,
}