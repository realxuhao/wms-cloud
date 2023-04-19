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
    }
  }
}

export default stockTake
