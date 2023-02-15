import { departmentService } from '@/api'

const department = {
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

export default department
