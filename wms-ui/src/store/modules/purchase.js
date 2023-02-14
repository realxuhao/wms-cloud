import { purchaseService } from '@/api'

const purchase = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getList ({ commit }, options) {
      const data = await purchaseService.getList(options)
      return data
    },
    async getOne ({ commit }, id) {
      const data = await purchaseService.getOne(id)
      return data
    },
    async add ({ commit }, options) {
      const data = await purchaseService.add(options)
      return data
    },
    async edit ({ commit }, { id, updateEntity }) {
      const data = await purchaseService.edit(id, updateEntity)
      return data
    },
    async destroy ({ commit }, id) {
      const data = await purchaseService.destroy(id)
      return data
    },
  }
}

export default purchase
