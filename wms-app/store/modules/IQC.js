import { IQCService } from '@/api'

const IQC = {
 namespaced: true,
  state: {
  },

  mutations: {
  },

  actions: {
    async getList ({ commit }, parameter) {
      const {data} = await IQCService.getList({...parameter})
      return data
    },
	async getSample ({ commit }, barCode) {
      const {data} = await IQCService.getSample(barCode)
      return data
    },
	async binDown({ commit }, barCode) {
      // const {data} = await manualTransService.binDown(barCode)
	  const {data} = await IQCService.binDown(barCode)
      return data
    },
	async getSampleList ({ commit }, parameter) {
	  const {data} = await IQCService.getSampleList({...parameter})
	  return data
	},
	async sampleConfirm ({ commit }, parameter) {
	  const {data} = await IQCService.sampleConfirm({...parameter})
	  return data
	},
	
	async allocateBin({ commit }, barCode) {
	  const {data} = await IQCService.allocateBin(barCode)
	  return data
	},
	async postBinIn({ commit }, options) {
	  const {data} = await IQCService.postBinIn(options)
	  return data
	},
	async getBinInInfo({ commit }, barCode) {
	  const {data} = await IQCService.getBinInInfo(barCode)
	  return data
	},
	
  }
}

export default IQC

