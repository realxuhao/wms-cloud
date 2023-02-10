import { receiveMaterialService } from '@/api'
import { removeToken } from '@/utils/storage'

const picking = {
 namespaced: true,
  state: {
  },

  mutations: {
  },

  actions: {
    async getPendingList ({ commit }, parameter) {
      const {data} = await receiveMaterialService.getList({...parameter})
      return data
    },
	async getHistoryList ({ commit }, parameter) {
      const {data} = await receiveMaterialService.getReceivedList({...parameter})
      return data
    },
  }
}

export default picking

