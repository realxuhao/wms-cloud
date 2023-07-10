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
    async getListBySupplierName ({ commit }, option) {
      const data = await purchaseService.getListBySupplierName(option.name, option.queryParams)
      return data
    },
    async close ({ commit }, id) {
      const data = await purchaseService.close(id)
      return data
    },
    async detail ({ commit }, id) {
      const data = await purchaseService.detail(id)
      return data
    },
    async syncdata ({ commit }) {
      const data = await purchaseService.syncdata()
      return data
    },
    async getErrorPoCode ({ commit }) {
      const data = await purchaseService.getErrorPoCode()
      return data
    },
    async getPoCodeList ({ commit }, options) {
      const data = await purchaseService.getPoCodeList(options)
      return data
    },
    async getPoItemList ({ commit }, options) {
      const data = await purchaseService.getPoItemList(options)
      return data
    },
    async getCmsNumberList ({ commit }, options) {
      const data = await purchaseService.getCmsNumberList(options)
      return data
    },
    async getSupplierName ({ commit }, option) {
      const data = await purchaseService.getSupplierName()
      return data
    }
  }
}

export default purchase
