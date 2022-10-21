import { areaService } from '@/api'

const area = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getList ({ commit }, options) {
      const { data: { rows } } = await areaService.getList({ ...options, pageSize: 0 })
      return rows
    },
    async getPaginationList ({ commit }, options) {
      const data = await areaService.getList(options)
      return data
    },
    async getOne ({ commit }, id) {
      const data = await areaService.getOne(id)
      return data
    },
    async add ({ commit }, options) {
      const data = await areaService.add(options)
      return data
    },
    async edit ({ commit }, { id, updateEntity }) {
      const data = await areaService.edit(id, updateEntity)
      return data
    },
    async destroy ({ commit }, id) {
      const data = await areaService.destroy(id)
      return data
    },
    async upload ({ commit }, formdata) {
      const data = await areaService.upload(formdata)
      return data
    },
    async uploadBatchUpdate ({ commit }, formdata) {
      const data = await areaService.uploadBatchUpdate(formdata)
      return data
    }
  }
}

export default area
