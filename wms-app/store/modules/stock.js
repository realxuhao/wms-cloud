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
  }
}

export default stock


