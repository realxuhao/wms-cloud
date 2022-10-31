import { frameService } from '@/api'

const frame = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getList ({ commit }, options) {
      const { data: { rows } } = await frameService.getList({ ...options, pageSize: 0 })
      return rows
    },
    async getPaginationList ({ commit }, options) {
      const data = await frameService.getList(options)
      return data
    },
    async getOne ({ commit }, id) {
      const data = await frameService.getOne(id)
      return data
    },
    async add ({ commit }, options) {
      const data = await frameService.add(options)
      return data
    },
    async edit ({ commit }, { id, updateEntity }) {
      const data = await frameService.edit(id, updateEntity)
      return data
    },
    async destroy ({ commit }, id) {
      const data = await frameService.destroy(id)
      return data
    },
    async upload ({ commit }, formdata) {
      const data = await frameService.upload(formdata)
      return data
    },
    async uploadBatchUpdate ({ commit }, formdata) {
      const data = await frameService.uploadBatchUpdate(formdata)
      return data
    }
  }
}

export default frame
