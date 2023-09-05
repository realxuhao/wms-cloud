import { driverDispatchService } from '@/api'

const driverDispatch = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async exportSign ({ commit }, options) {
      const data = await driverDispatchService.exportSign(options)
      return data
    },
    async exportNotSign ({ commit }, options) {
      const data = await driverDispatchService.exportNotSign(options)
      return data
    },
    async getList ({ commit }, options) {
      const data = await driverDispatchService.getList(options.parameter, options.param)
      return data
    },
    async getTodaySignlist ({ commit }, options) {
      const data = await driverDispatchService.getTodaySignlist(options)
      return data
    },
    async getTodayNoSignList ({ commit }, options) {
      const data = await driverDispatchService.getTodayNoSignList(options.parameter, options.param)
      return data
    },
    async importDock ({ commit }, options) {
      const data = await driverDispatchService.importDock(options)
      return data
    },
    async cancel ({ commit }, id) {
      const data = await driverDispatchService.cancel(id)
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
    async change ({ commit }, id) {
      const data = await driverDispatchService.change(id)
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
