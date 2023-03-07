import { fsmpService } from '@/api'

const ecnRule = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getList ({ commit }, options) {
      const data = await fsmpService.getList(options)
      return data
    },
    async getOne ({ commit }, id) {
      const data = await fsmpService.getOne(id)
      return data
    },
    async add ({ commit }, options) {
      const data = await fsmpService.add(options)
      return data
    },
    async edit ({ commit }, { id, updateEntity }) {
      const data = await fsmpService.edit(id, updateEntity)
      return data
    },
    async destroy ({ commit }, id) {
      const data = await fsmpService.destroy(id)
      return data
    },
    async upload ({ commit }, formdata) {
      const data = await fsmpService.upload(formdata)
      return data
    },
    async uploadBatchUpdate ({ commit }, formdata) {
      const data = await fsmpService.uploadBatchUpdate(formdata)
      return data
    }
  }
}

export default ecnRule
