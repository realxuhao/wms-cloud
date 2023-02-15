import { purchaseService } from '@/api'

const purchase = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getList ({ commit }, options) {
      const data = await purchaseService.getList(options)
      return data
    },
    async getListBySupplierName ({ commit }, name) {
      const data = await purchaseService.getListBySupplierName(name)
      return data
    }
  }
}

export default purchase
