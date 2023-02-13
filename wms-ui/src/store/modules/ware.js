import { wareService } from '@/api'

const ware = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getList ({ commit }, options) {
      const { data: { rows } } = await wareService.getList({ ...options, pageSize: 0 })
      return rows
    },
    async getPaginationList ({ commit }, options) {
      const data = await wareService.getList(options)
      return data
    },
    async getOne ({ commit }, id) {
      const data = await wareService.getOne(id)
      return data
    },
    async add ({ commit }, options) {
      const data = await wareService.add(options)
      return data
    },
    async edit ({ commit }, { id, updateEntity }) {
      const data = await wareService.edit(id, updateEntity)
      return data
    },
    async destroy ({ commit }, id) {
      const data = await wareService.destroy(id)
      return data
    },
    async getOptionList ({ commit }) {
      const data = await wareService.getOptionList()
      return data
    }
  }
}

export default ware
