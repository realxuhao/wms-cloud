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

const getByBinCode = async (binCode) => {
  const url = `/bin-in/stock/getByBinCode/${binCode}`
  const method = 'GET'

  return request({
    url,
    method,
  })
}

const addSplit = async (options) => {
  const url = `/bin-in/split/add`
  const method = 'POST'

  return request({
    url,
    method,
	data:options
  })
}

export const stockService = {
  getStockInfoByMesBarCode,
  getStockInfoBySscc,
  getByBinCode,
  addSplit
}
