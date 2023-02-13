import { timeWindowService } from '@/api'

const timeWindow = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getList ({ commit }, options) {
      const data = await timeWindowService.getList(options)
      return data
    },
    async getOne ({ commit }, id) {
      const data = await timeWindowService.getOne(id)
      return data
    },
    async add ({ commit }, options) {
      const data = await timeWindowService.add(options)
      return data
    },
    async edit ({ commit }, { id, updateEntity }) {
      const data = await timeWindowService.edit(id, updateEntity)
      return data
    },
    async destroy ({ commit }, id) {
      const data = await timeWindowService.destroy(id)
      return data
    },
    async getDataByWareId ({ commit }, id) {
      const data = await timeWindowService.getDataByWareId(id)
      return data
    },
    async addList ({ commit }, List) {
      const data = await timeWindowService.addList(List)
      return data
    }
  }
}

export default timeWindow
