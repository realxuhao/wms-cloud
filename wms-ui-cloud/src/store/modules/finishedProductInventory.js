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
    },
    async productStockExport ({ commit }, options) {
      const data = await finishedProductInventoryService.productStockExport(options)
      return data
    },
    async saleStockExport ({ commit }, options) {
      const data = await finishedProductInventoryService.saleStockExport(options)
      return data
    },
  }
}

export default finishedProductInventory
