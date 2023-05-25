import { palletService } from '@/api'

const pallet = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getList ({ commit }, options) {
      const { data: { rows } } = await palletService.getList({ pageSize: 0 })
      return rows
    },
    async getPaginationList ({ commit }, options) {
      const data = await palletService.getList(options)
      return data
    },
    async getOne ({ commit }, id) {
      const data = await palletService.getOne(id)
      return data
    },
    async add ({ commit }, options) {
      const data = await palletService.add(options)
      return data
    },
    async edit ({ commit }, { id, updateEntity }) {
      const data = await palletService.edit(id, updateEntity)
      return data
    },
    async destroy ({ commit }, id) {
      const data = await palletService.destroy(id)
      return data
    }
  }
}

export default pallet
