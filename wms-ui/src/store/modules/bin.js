import { binService } from '@/api'

const bin = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getList ({ commit }, options) {
      const { data: { rows } } = await binService.getList({ ...options, pageSize: 0 })
      return rows
    },
    async getPaginationList ({ commit }, options) {
      const data = await binService.getList(options)
      return data
    },
    async getOne ({ commit }, id) {
      const data = await binService.getOne(id)
      return data
    },
    async add ({ commit }, options) {
      const data = await binService.add(options)
      return data
    },
    async edit ({ commit }, { id, updateEntity }) {
      const data = await binService.edit(id, updateEntity)
      return data
    },
    async destroy ({ commit }, id) {
      const data = await binService.destroy(id)
      return data
    }
  }
}

export default bin
