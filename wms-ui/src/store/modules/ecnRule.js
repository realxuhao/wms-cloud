import { ecnService } from '@/api'

const ecnRule = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getList ({ commit }, options) {
      const data = await ecnService.getList(options)
      return data
    },
    async getOne ({ commit }, id) {
      const data = await ecnService.getOne(id)
      return data
    },
    async add ({ commit }, options) {
      const data = await ecnService.add(options)
      return data
    },
    async edit ({ commit }, { id, updateEntity }) {
      const data = await ecnService.edit(id, updateEntity)
      return data
    },
    async destroy ({ commit }, id) {
      const data = await ecnService.destroy(id)
      return data
    },
    async upload ({ commit }, formdata) {
      const data = await ecnService.upload(formdata)
      return data
    },
    async uploadBatchUpdate ({ commit }, formdata) {
      const data = await ecnService.uploadBatchUpdate(formdata)
      return data
    }
  }
}

export default ecnRule
