import { stockTakeDetailService } from '@/api'

const stockTake = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getList ({ commit }, options) {
      const data = await stockTakeDetailService.getList(options)
      return data
    },
    async confirm ({ commit }, options) {
      const data = await stockTakeDetailService.confirm(options)
      return data
    },
    async editTakeQuantity ({ commit }, options) {
      const data = await stockTakeDetailService.editTakeQuantity(options)
      return data
    },
    async issue ({ commit }, options) {
      const data = await stockTakeDetailService.issue(options)
      return data
    }
  }
}

export default stockTake
