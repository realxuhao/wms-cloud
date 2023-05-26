import { stockTakeService } from '@/api'

const stockTake = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getList ({ commit }, options) {
      const { data: { rows } } = await stockTakeService.getList({ ...options, pageSize: 0 })
      return rows
    },
    async getPaginationList ({ commit }, options) {
      console.log('111')
      const data = await stockTakeService.getList(options)
      return data
    },
    async add ({ commit }, options) {
      const data = await stockTakeService.add(options)
      return data
    },
    async destroy ({ commit }, id) {
      const data = await stockTakeService.destroy(id)
      return data
    },
    async exportList ({ commit }, options) {
      const data = await stockTakeService.exportList(options)
      return data
    },
    async exportDetailList ({ commit }, options) {
      const data = await stockTakeService.exportDetailList(options)
      return data
    },
  }
}

export default stockTake
