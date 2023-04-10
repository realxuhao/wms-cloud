import { finishedProductTransferService } from '@/api'

const finishedProductTransfer = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getTransferList ({ commit }, options) {
      const data = await finishedProductTransferService.getTransferList(options)
      return data
    },
    async createTransfer ({ commit }, options) {
      const data = await finishedProductTransferService.createTransfer(options)
      return data
    },
    async cancelTransfer ({ commit }, options) {
      const data = await finishedProductTransferService.cancelTransfer(options)
      return data
    }
  }
}

export default finishedProductTransfer
