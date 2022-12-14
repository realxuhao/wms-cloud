import { wareShiftService } from '@/api'

const wareShift = {
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
      console.log('1wwq1')
      const { data: { rows } } = await wareShiftService.getList({ ...options, pageSize: 0 })
      return rows
    },
    async getPaginationList ({ commit }, options) {
      console.log('111')
      const data = await wareShiftService.getList(options)
      return data
    }
  }
}

export default wareShift
