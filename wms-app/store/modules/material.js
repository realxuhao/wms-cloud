import { materialService } from '@/api'

const material = {
 namespaced: true,
  state: {
  },

  mutations: {
  },

  actions: {
	async parsedBarCode({ commit }, mesBarCode) {
	  const {data} = await materialService.parsedBarCode(mesBarCode)
	  return data
	},
  }
}

export default material


