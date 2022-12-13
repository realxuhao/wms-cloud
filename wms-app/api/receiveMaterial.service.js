import request from './general'

const getList = async (data) => {
  const url = `/binin/materialKanban/receivingMaterial`
  const method = 'POST'

  return request({
    url,
    method,
    data
  })
}

const getReceivedList = async (data) => {
  const url = '/binin/materialKanban/receivedMaterial'
  const method = 'POST'
  return request({
    url,
    method,
    data
  })
}

export const receiveMaterialService = {
  getList,
  getReceivedList
}