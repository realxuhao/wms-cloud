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
      const { data: { rows } } = await wareShiftService.getList({ ...options, pageSize: 0 })
      return rows
    },
    async getPaginationList ({ commit }, options) {
      const data = await wareShiftService.getList(options)
      return data
    },
    async addWareShift ({ commit }, options) {
      const data = await wareShiftService.addWareShift(options)
      return data
    },
    async getReturnMaterialList ({ commit }, options) {
      const data = await wareShiftService.getReturnMaterialList(options)
      return data
    },
    async cancel ({ commit }, options) {
      const data = await wareShiftService.cancel(options)
      return data
    },

    async exportExcel ({ commit }, options) {
      const data = await wareShiftService.exportExcel(options)
      return data
    },
    async exportReturnExcel ({ commit }, options) {
      const data = await wareShiftService.exportReturnExcel(options)
      return data
    },
    async generateWareShiftByCall ({ commit }, options) {
      const data = await wareShiftService.generateWareShiftByCall(options)
      return data
    },


  }
}

export default wareShift
