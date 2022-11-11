import request from './general'

const getList = async (data) => {
  const url = `/masterdata/ware/list`
  const method = 'GET'

  return request({
    url,
    method,
    data
  })
}

export const plantService = {
  getList,
}
