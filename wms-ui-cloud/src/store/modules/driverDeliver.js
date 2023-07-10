import { driverDeliverService } from '@/api'

const driverDeliver = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getList ({ commit }, options) {
      const data = await driverDeliverService.getList(options)
      return data
    },
    async getOnceSupplierOnTime ({ commit }, options) {
      const data = await driverDeliverService.getOnceSupplierOnTime(options)
      return data
    },
    async exportOnTime ({ commit }, options) {
      const data = await driverDeliverService.exportOnTime(options)
      return data
    },
    async destroy ({ commit }, id) {
      const data = await driverDeliverService.destroy(id)
      return data
    }
  }
}

export default driverDeliver
