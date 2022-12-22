import { manualTransService } from '@/api'

const manualTrans = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  // actions: {
  //   async getList ({ commit }, options) {
  //     const { data: { rows } } = await wareShiftService.getList({ ...options, pageSize: 0 })
  //     return rows
  //   },
  //   async getPaginationList ({ commit }, options) {
  //     console.log('1w1')
  //     const data = await wareShiftService.getList(options)
  //     return data
  //   }
  // }
  actions: {
    async getList ({ commit }, options) {
      const { data: { rows } } = await manualTransService.getList({ ...options, pageSize: 0 })
      return rows
    },
    async getPaginationList ({ commit }, options) {
      const data = await manualTransService.getList(options)
      return data
    },
    async batchIssueJob ({ commit }, options) {
      const data = await manualTransService.batchIssueJob(options)
      return data
    },
    async cancelManualTrans ({ commit }, options) {
      const data = await manualTransService.cancelManualTrans(options)
      return data
    }
  }
}

export default manualTrans
