import { driverDispatchService } from '@/api'

const driverDispatch = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getTodaySignlist ({ commit }, options) {
      const data = await driverDispatchService.getTodaySignlist(options)
      return data
    },
    async getTodayNoSignList ({ commit }) {
      const data = await driverDispatchService.getTodayNoSignList()
      return data
    },
    async importDock ({ commit }, options) {
      const data = await driverDispatchService.importDock(options)
      return data
    },
    async enter ({ commit }, id) {
      const data = await driverDispatchService.enter(id)
      return data
    },
    async complete ({ commit }, id) {
      const data = await driverDispatchService.complete(id)
      return data
    }
  }
}

export default driverDispatch
