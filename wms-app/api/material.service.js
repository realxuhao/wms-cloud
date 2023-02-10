import request from './general'

const parsedBarCode = async (mesBarcode) => {
  const url = `/master-data/mesBarCode/parseMesBarCode/${mesBarcode}`
  const method = 'GET'

  return request({
    url,
    method,
  })
}

export const materialService = {
  parsedBarCode,
}
