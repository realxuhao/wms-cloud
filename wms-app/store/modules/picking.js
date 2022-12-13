import { pickingService } from '@/api'

const picking = {
 namespaced: true,
  state: {
  },

  mutations: {
  },

  actions: {
    async getPendingList ({ commit }, parameter) {
      const {data} = await pickingService.getList({...parameter,status:1})
      return data
    },
	async getHistoryList ({ commit }, parameter) {
      const {data} = await pickingService.getDownList(parameter)
      return data
    },
  }
}

export default picking

