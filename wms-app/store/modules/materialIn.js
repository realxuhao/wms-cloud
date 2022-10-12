import { materialInService } from '@/api'
import { removeToken } from '@/utils/storage'

const materialIn = {
 namespaced: true,
  state: {
  },

  mutations: {
  },

  actions: {
    async getPendingMaterialList ({ commit }, parameter) {
      const {data} = await materialInService.getPendingMaterialList(parameter)
      return data
    },
	async getMaterialInHistoryList ({ commit }, parameter) {
      const {data} = await materialInService.getMaterialInHistoryList(parameter)
      return data
    },
	async getAndCheckMaterialIn({commit},ssccCode){
		const {data} = await materialInService.getAndCheckMaterialIn(ssccCode)
		  return data
	},
	async postMaterialIn({ commit }, parameter) {
      const {data} = await materialInService.postMaterialIn(parameter)
      return data
    },
  }
}

export default materialIn
