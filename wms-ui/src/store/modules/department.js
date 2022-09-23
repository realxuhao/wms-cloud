import { departmentService } from '@/api'

const material = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getList ({ commit }, options) {
      const data = await departmentService.getList(options)
      return data
    }
  }
}

export default material
