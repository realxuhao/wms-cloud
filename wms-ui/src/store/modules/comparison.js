import { comparisonService } from '@/api'

const comparison = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getList ({ commit }, options) {
      const { data: { rows } } = await comparisonService.getList({ ...options, pageSize: 0 })
      return rows
    },
    async getPaginationList ({ commit }, options) {
      console.log('111')
      const data = await comparisonService.getList(options)
      return data
    },
    async upload ({ commit }, formdata) {
      const data = await comparisonService.upload(formdata)
      return data
    },
  }
}

export default comparison
