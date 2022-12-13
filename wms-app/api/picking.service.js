import request from './general'

const getList = async (data) => {
  const url = `/binin/materialKanban/list`
  const method = 'POST'

  return request({
    url,
    method,
    data
  })
}

const getDownList = async (data) => {
  const url = '/binin/materialKanban/binDownList'
  const method = 'POST'
  return request({
    url,
    method,
    data
  })
}

export const pickingService = {
  getList,
  getDownList
}
