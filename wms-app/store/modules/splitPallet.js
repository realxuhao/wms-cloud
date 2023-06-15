import { splitPalletService } from '@/api'

const splitPallet = {
 namespaced: true,
  state: {
  },

  mutations: {
  },

  actions: {
    async getList ({ commit }, parameter) {
      const {data} = await splitPalletService.getList({...parameter})
      return data
    },
	async getInfo ({ commit }, barCode) {
	  const {data} = await splitPalletService.getInfo(barCode)
	  return data
	},
	async getBinInInfo({ commit }, barCode) {
	  const {data} = await splitPalletService.getBinInInfo(barCode)
	  return data
	},
	async postBinIn({ commit }, options) {
	  const {data} = await splitPalletService.postBinIn(options)
	  return data
	},
	async splitPallet({ commit }, options) {
	  const {data} = await splitPalletService.splitPallet(options)
	  return data
	},
	
	
	
  }
}

export default splitPallet

