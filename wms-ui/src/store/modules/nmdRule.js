import { nmdService } from '@/api'

const nmdRule = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getList ({ commit }, options) {
      const data = await nmdService.getList(options)
      return data
    },
    async getOne ({ commit }, id) {
      const data = await nmdService.getOne(id)
      return data
    },
    async add ({ commit }, options) {
      const data = await nmdService.add(options)
      return data
    },
    async edit ({ commit }, { id, updateEntity }) {
      const data = await nmdService.edit(id, updateEntity)
      return data
    },
    async destroy ({ commit }, id) {
      const data = await nmdService.destroy(id)
      return data
    },
    async upload ({ commit }, formdata) {
      const data = await nmdService.upload(formdata)
      return data
    },
    async uploadBatchUpdate ({ commit }, formdata) {
      const data = await nmdService.uploadBatchUpdate(formdata)
      return data
    }
  }
}

export default nmdRule
