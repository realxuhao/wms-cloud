import { finishedProductStorageService } from '@/api'

const finishedProductStorage = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async getStorageList ({ commit }, options) {
      const data = await finishedProductStorageService.getStorageList(options)
      return data
    },
    async deleteStorage ({ commit }, options) {
      const data = await finishedProductStorageService.deleteStorage(options)
      return data
    },
    async storageImport ({ commit }, options) {
      const data = await finishedProductStorageService.storageImport(options)
      return data
    },
    async uploadBatchUpdate ({ commit }, options) {
      const data = await finishedProductStorageService.uploadBatchUpdate(options)
      return data
    }
  }
}

export default finishedProductStorage
