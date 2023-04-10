import _ from 'lodash'
import notification from 'ant-design-vue/es/notification'
import axios from 'axios'
import * as uuid from 'uuid'
import store from '../store'
import { getToken } from '@/utils/cookie'

const okayHttpStatuses = [
  200,
  201,
  204,
  304
]

const errorParser = async (response) => {
  const { status, data } = response

  if (_.some(okayHttpStatuses, s => s === status)) {
    if (data.code === 401) {
      const token = getToken()
      notification.error({
        message: '身份认证错误',
        description: '身份认证已失效，即将返回登录'
      })
      if (token) {
        store.dispatch('Logout').then(() => {
          setTimeout(() => {
            window.location.reload()
          }, 1500)
        })
      }
      return Promise.reject(new Error('Unauthorized'))
    }

    // if (data instanceof Blob) {
    //   const { errorMessage } = await new Response(data).json()
    //   return Promise.reject(new ApiError(errorMessage, { isHandled: false, status }))
    // }

    // if (data instanceof ArrayBuffer) {
    //   const { errorMessage } = await new Response(data).json()
    //   return Promise.reject(new ApiError(errorMessage, { isHandled: false, status }))
    // }

    if (data.code && data.code !== 200) {
      // eslint-disable-next-line prefer-promise-reject-errors
      return Promise.reject({
        ...data,
        message: data.msg || '未知的错误，请联系要管理员！'
      })
    }

    return response
  }

  if (status === 403) {
    notification.error({
      message: 'Forbidden',
      description: data.errorMessage
    })
    // eslint-disable-next-line prefer-promise-reject-errors
    return Promise.reject({ message: 'Forbidden' })
  }

  if (status === 401) {
    const token = getToken()
    notification.error({
      message: '身份认证错误',
      description: '身份认证已失效，即将返回登录'
    })
    if (token) {
      store.dispatch('Logout').then(() => {
        setTimeout(() => {
          window.location.reload()
        }, 1500)
      })
    }
    return Promise.reject(new Error('Unauthorized'))
  }

  const errorMessage = (data && data.msg) ? data.msg : JSON.stringify(data)
  return Promise.reject(new Error(errorMessage))
}

export const createInstance = (baseUrl, authenticated) => {
  const timeout = 600 * 1000
  const headers = {
    'x-tracking-id': uuid.v4()
  }
  if (authenticated) {
    headers['Authorization'] = 'Bearer ' + getToken()
  }
  const instance = axios.create({
    baseURL: baseUrl,
    timeout,
    headers,
    validateStatus: () => true
  })
  instance.interceptors.response.use(errorParser)
  return instance
}

export const createAuthInstance = (baseUrl) => createInstance(baseUrl, true)
