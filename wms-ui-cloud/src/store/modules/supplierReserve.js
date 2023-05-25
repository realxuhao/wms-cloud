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
    },
    async destroy ({ commit }, id) {
      const data = await supplierReserveService.destroy(id)
      return data
    },
    async getDetailsList ({ commit }, reserveNo) {
      const data = await supplierReserveService.getDetailsList(reserveNo)
      return data
    }
  }
}

export default supplierReserve
