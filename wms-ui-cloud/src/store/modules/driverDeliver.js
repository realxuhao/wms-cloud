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
    async destroy ({ commit }, id) {
      const data = await driverDeliverService.destroy(id)
      return data
    }
  }
}

export default driverDeliver
