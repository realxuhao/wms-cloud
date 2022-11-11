import { plantService } from '@/api'

const plant = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getList ({ commit }, options) {
      const { data: { rows } } = await plantService.getList({ ...options, pageSize: 0 })
      return rows
    },
  }
}

export default plant
