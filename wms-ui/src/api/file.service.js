import config from '@/config/api.config'
import { createAuthInstance } from './general'

const baseUrl = `${config.apiHost}/wms-file`

const getFileUrl = async (filename) => {
  const url = `/file/download`
  const { data } = await createAuthInstance(baseUrl).post(url, { fileName: filename })
  return data
}

const getFileByUrl = async (minioUrl) => {
  const { data } = await createAuthInstance(minioUrl).get(minioUrl, {
    responseType: 'blob'
  })
  return data
}

export const fileService = {
  getFileUrl,
  getFileByUrl
}
