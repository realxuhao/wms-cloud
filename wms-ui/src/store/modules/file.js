import { fileService } from '@/api'
import { download } from '@/utils/file'

const file = {
  namespaced: true,
  state: {
  },

  mutations: {

  },

  actions: {
    async downloadByFilename ({ commit }, filename) {
      const { data: url } = await fileService.getFileUrl(filename)
      console.log(url)
      const blobData = await fileService.getFileByUrl(url)
      download(blobData)
    }
  }
}

export default file
