import request from './general'

const login = async (data) => {
  const url = `/auth/login`
  const method = 'POST'

  return request({
    url,
    method,
    data
  })
}

const getInfo = async () => {
  const url = '/system/user/getInfo'
  const method = 'GET'

  return request({
    url,
    method
  })
}

export const authService = {
  login,
  getInfo,
}
