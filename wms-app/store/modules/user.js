import { authService } from '@/api'
import { removeToken } from '@/utils/storage'

const user = {
  state: {
    name: '',
    info: {
    },
  },

  mutations: {
    setInfo (state, info) {
      state.info = info
    },
  },

  actions: {
    async Login ({ commit }, parameter) {
      const data = await authService.login(parameter)
      return data.data.access_token
    },

    // 获取用户信息
    async GetInfo ({ commit }) {
      const userInfo = await authService.getInfo()
      commit('setInfo', userInfo)

      return userInfo
    },

    // 登出
    async Logout ({ commit }) {
      removeToken()
      uni.navigateTo({
        url: '/pages/login/index'
      });
    }
  }
}

export default user
