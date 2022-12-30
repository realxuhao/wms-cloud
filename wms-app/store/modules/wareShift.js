import { wareShiftService } from '@/api'

const wareShift = {
 namespaced: true,
  state: {
  },

  mutations: {
  },

  actions: {
    async getList ({ commit }, parameter) {
      const {data} = await wareShiftService.getList({...parameter})
      return data
    },
	async getWaitingBinIn ({ commit }, parameter) {
      const {data} = await wareShiftService.getWaitingBinIn({...parameter})
      return data
    },
	async binDown({ commit }, barCode) {
      const {data} = await wareShiftService.binDown(barCode)
      return data
    },
	async getByMesBarCode({ commit }, barCode) {
	  const {data} = await wareShiftService.allocateBin(barCode)
	  return data
	},
	async getReturnMaterialList({commit},options){
		const {data} = await wareShiftService.getReturnMaterialList(options)
		return data
	}
  }
}

export default wareShift

