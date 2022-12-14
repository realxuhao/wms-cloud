import { stockService } from '@/api'

const stock = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getList ({ commit }, options) {
      const { data: { rows } } = await stockService.getList({ ...options, pageSize: 0 })
      return rows
    },
    async getPaginationList ({ commit }, options) {
      console.log('111')
      const data = await stockService.getList(options)
      return data
    }
  }
}

export default stock
