import { iqcSampleService } from '@/api'

const iqcSample = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async exportExcel ({ commit }, options) {
      const data = await iqcSampleService.exportExcel(options)
      return data
    },
    async cancelIqc ({ commit }, options) {
      const data = await iqcSampleService.cancelIqc(options)
      return data
    },
    async addManual ({ commit }, options) {
      const data = await iqcSampleService.addIqcManual(options)
      return data
    },
    async getList ({ commit }, options) {
      const data = await iqcSampleService.getList(options)
      return data
    },
    async modifySscc ({ commit }, options) {
      const data = await iqcSampleService.modifySscc(options)
      return data
    },
    async addShift ({ commit }, options) {
      const data = await iqcSampleService.addShift(options)
      return data
    },
    async cancelShift ({ commit }, options) {
      const data = await iqcSampleService.cancelShift(options)
      return data
    }
  }
}

export default iqcSample
