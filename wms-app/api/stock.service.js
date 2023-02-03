import request from './general'

const getStockInfoByMesBarCode = async (mesBarcode) => {
  const url = `/bin-in/stock/getByMesBarCode/${mesBarcode}`
  const method = 'GET'

  return request({
    url,
    method,
  })
}

const getStockInfoBySscc = async (sscc) => {
  const url = `/bin-in/stock/getBySscc/${sscc}`
  const method = 'GET'

  return request({
    url,
    method,
  })
}

export const stockService = {
  getStockInfoByMesBarCode,
  getStockInfoBySscc
}
