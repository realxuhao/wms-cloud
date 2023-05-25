import { driverDispatchService } from '@/api'

const driverDispatch = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getList ({ commit }, parameter, options) {
      const data = await driverDispatchService.getList(parameter, options)
      return data
    },
    async getTodaySignlist ({ commit }, options) {
      const data = await driverDispatchService.getTodaySignlist(options)
      return data
    },
    async getTodayNoSignList ({ commit }, options) {
      const data = await driverDispatchService.getTodayNoSignList(options)
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
    },
    async sort ({ commit }, options) {
      const data = await driverDispatchService.sort(options)
      return data
    },
    async getWxToken ({ commit }) {
      const data = await driverDispatchService.getWxToken()
      return data
    },
    async sendMsgToWx ({ commit }, options) {
      const data = await driverDispatchService.sendMsgToWx(options)
      return data
    }
  }
}

export default driverDispatch
