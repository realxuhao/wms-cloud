import { manualTransService } from '@/api'

const IQC = {
 namespaced: true,
  state: {
  },

  mutations: {
  },

  actions: {
    async getList ({ commit }, parameter) {
      const {data} = await manualTransService.getList({...parameter})
      return data
    },
	async getManualTransInfo ({ commit }, barCode) {
      const {data} = await manualTransService.info(barCode)
      return data
    },
	async binDown({ commit }, barCode) {
      // const {data} = await manualTransService.binDown(barCode)
	  const {data} = await manualTransService.binDown(barCode)
      return data
    },
	async allocateBin({ commit }, barCode) {
	  const {data} = await manualTransService.allocateBin(barCode)
	  return data
	},
	async postBinIn({ commit }, options) {
	  const {data} = await manualTransService.postBinIn(options)
	  return data
	},
	
  }
}

export default IQC

