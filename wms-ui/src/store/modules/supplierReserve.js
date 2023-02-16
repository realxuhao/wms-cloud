import { supplierReserveService } from '@/api'

const supplierReserve = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getList ({ commit }, options) {
      const data = await supplierReserveService.getList(options)
      return data
    },
    async getListByParam ({ commit }, options) {
      const data = await supplierReserveService.getListByParam(options)
      return data
    },
    async addList ({ commit }, options) {
      const data = await supplierReserveService.addList(options)
      return data
    }
  }
}

export default supplierReserve
