import { materialService } from '@/api'

const material = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getList ({ commit }, options) {
      const data = await materialService.getList(options)
      return data
    },
    async getOne ({ commit }, id) {
      const data = await materialService.getOne(id)
      return data
    },
    async add ({ commit }, options) {
      const data = await materialService.add(options)
      return data
    },
    async edit ({ commit }, { id, updateEntity }) {
      const data = await materialService.edit(id, updateEntity)
      return data
    },
    async destroy ({ commit }, id) {
      const data = await materialService.destroy(id)
      return data
    },

    async getDispatchFrameTypeList ({ commit }, options) {
      const data = await materialService.getDispatchFrameTypeList(options)
      return data
    },
    async addDiapatchBin ({ commit }, options) {
      const data = await materialService.addDispatchBin(options)
      return data
    },
    async destroyDispatchBin ({ commit }, id) {
      const data = await materialService.destroyDispatchBin(id)
      return data
    },
    async uploadMaterialBin ({ commit }, formdata) {
      const data = await materialService.uploadMaterialBin(formdata)
      return data
    },
    async uploadMaterialBinBatchUpdate ({ commit }, formdata) {
      const data = await materialService.uploadBatchUpdate(formdata)
      return data
    },
    async upload ({ commit }, formdata) {
      const data = await materialService.upload(formdata)
      return data
    },
    async uploadBatchUpdate ({ commit }, formdata) {
      const data = await materialService.uploadBatchUpdate(formdata)
      return data
    }

  }
}

export default material
