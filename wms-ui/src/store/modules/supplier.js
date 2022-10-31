import { supplierService } from '@/api'

const supplier = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getList ({ commit }, options) {
      const data = await supplierService.getList(options)
      return data
    },
    async getOne ({ commit }, id) {
      const data = await supplierService.getOne(id)
      return data
    },
    async add ({ commit }, options) {
      const data = await supplierService.add(options)
      return data
    },
    async edit ({ commit }, { id, updateEntity }) {
      const data = await supplierService.edit(id, updateEntity)
      return data
    },
    async destroy ({ commit }, id) {
      const data = await supplierService.destroy(id)
      return data
    },
    async upload ({ commit }, formdata) {
      const data = await supplierService.upload(formdata)
      return data
    },
    async uploadBatchUpdate ({ commit }, formdata) {
      const data = await supplierService.uploadBatchUpdate(formdata)
      return data
    }
  }
}

export default supplier
