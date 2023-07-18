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
      const data = await comparisonService.getList(options)
      return data
    },
    async getPaginationProList ({ commit }, options) {
      const data = await comparisonService.getProList(options)
      return data
    },
    async upload ({ commit }, formdata) {
      const data = await comparisonService.upload(formdata)
      return data
    },
    async uploadPro ({ commit }, formdata) {
      const data = await comparisonService.uploadPro(formdata)
      return data
    },
    async updateBySsccList ({ commit }, formdata) {
      const data = await comparisonService.updateBySsccList(formdata)
      return data
    },
    async updateByIdList ({ commit }, options) {
      console.log('1234',options)
      const data = await comparisonService.updateByIdList(options)
      return data
    }

  }
}

export default comparison
