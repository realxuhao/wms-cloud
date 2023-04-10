import { finishedProductInventoryService } from '@/api'

const finishedProductInventory = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getInventoryList ({ commit }, options) {
      const data = await finishedProductInventoryService.getInventoryList(options)
      return data
    }
  }
}

export default finishedProductInventory
