import { materialCallService } from '@/api'

const materialCall = {
 namespaced: true,
  state: {
  },

  mutations: {
  },

  actions: {
    async waitingBinDownList ({ commit }, parameter) {
      const {data} = await materialCallService.waitingBinDownList({...parameter})
      return data
    },
	async binDownList ({ commit }, parameter) {
      const {data} = await materialCallService.binDownList(parameter)
      return data
    },
	async binDown ({ commit }, ssccNumber) {
	  const {data} = await materialCallService.binDown(ssccNumber)
	  return data
	},
	async confirmMaterial({ commit }, options) {
	  const {data} = await materialCallService.confirmMaterial(options)
	  return data
	},
  }
}

export default materialCall

