import { productService } from '@/api'

const product = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getList ({ commit }, options) {
      const { data: { rows } } = await productService.getList({ pageSize: 0 })
      return rows
    },
    async getProductList ({ commit }, options) {
      const { data: { rows } } = await productService.getProductList({ pageSize: 0 })
      return rows
    },

    async getPaginationList ({ commit }, options) {
      const data = await productService.getList(options)
      return data
    },
    async getOne ({ commit }, id) {
      const data = await productService.getOne(id)
      return data
    },
    async add ({ commit }, options) {
      const data = await productService.add(options)
      return data
    },
    async edit ({ commit }, { id, updateEntity }) {
      const data = await productService.edit(id, updateEntity)
      return data
    },
    async destroy ({ commit }, id) {
      const data = await productService.destroy(id)
      return data
    },
    async upload ({ commit }, formdata) {
      const data = await productService.upload(formdata)
      return data
    },
    async uploadBatchUpdate ({ commit }, formdata) {
      const data = await productService.uploadBatchUpdate(formdata)
      return data
    },
    async addDispatchBin ({ commit }, formdata) {
      const data = await productService.addDispatchBin(formdata)
      return data
    }

  }
}

export default product
