import { productPackagingService } from '@/api'

const productPackaging = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getList ({ commit }, options) {
      const data = await productPackagingService.getList(options)
      return data
    },
    async uploadBatchUpdate ({ commit }, formdata) {
      const data = await productPackagingService.uploadBatchUpdate(formdata)
      return data
    },
    async upload ({ commit }, formdata) {
      const data = await productPackagingService.upload(formdata)
      return data
    },
    async getOne ({ commit }, id) {
      const data = await productPackagingService.getOne(id)
      return data
    },
    async add ({ commit }, options) {
      const data = await productPackagingService.add(options)
      return data
    },
    async edit ({ commit }, { id, updateEntity }) {
      const data = await productPackagingService.edit(id, updateEntity)
      return data
    },
    async destroy ({ commit }, id) {
      const data = await productPackagingService.destroy(id)
      return data
    }
  }
}

export default productPackaging
