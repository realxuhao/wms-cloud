import { stockService } from '@/api'

const stock = {
 namespaced: true,
  state: {
  },

  mutations: {
  },

  actions: {
	async getInfoByMesBarCode({ commit }, mesBarCode) {
	  const {data} = await stockService.getStockInfoByMesBarCode(mesBarCode)
	  return data
	},
	async getInfoBySscc({ commit }, sscc) {
	  const {data} = await stockService.getStockInfoBySscc(sscc)
	  return data
	},
	async getByMesBarCode({ commit }, mesBarCode) {
	  const {data} = await stockService.getStockInfoByMesBarCode(mesBarCode)
	  return data
	},
	async getByBinCode({ commit }, binCode) {
	  const {data} = await stockService.getByBinCode(binCode)
	  return data
	},
	async addSplit({ commit }, options) {
	  const {data} = await stockService.addSplit(options)
	  return data
	},
  }
}

export default stock


