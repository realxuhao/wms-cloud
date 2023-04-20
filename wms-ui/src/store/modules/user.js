import { authService } from '@/api'
import { removeToken } from '@/utils/cookie'

const user = {
  state: {
    token: '',
    name: '',
    welcome: '',
    avatar: '',
    roles: [],
    info: {}
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_INFO: (state, info) => {
      state.info = info
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
    }
  },

  actions: {
    async GetCodeImg () {
      const data = await authService.getCodeImg()
      return data
    },

    async Login ({ commit }, parameter) {
      const data = await authService.login(parameter)
      commit('SET_TOKEN', data.data.access_token)
      return data.data.access_token
    },

    // 获取用户信息
    async GetInfo ({ commit }) {
      const res = await authService.getInfo()
      const user = res.user
      const avatar = user.avatar === '' ? require('@/assets/images/profile.png') : process.env.VUE_APP_BASE_API + user.avatar
      if (res.roles && res.roles.length > 0) { // 验证返回的roles是否是一个非空数组
        commit('SET_ROLES', res.roles)
        commit('SET_PERMISSIONS', res.permissions)
      } else {
        commit('SET_ROLES', ['ROLE_DEFAULT'])
      }
      commit('SET_NAME', user.nickName)
      commit('SET_AVATAR', avatar)
      return res
    },

    async GetAcl ({ commit }, { serviceName }) {
      const acl = await authService.getAcl({ serviceName })
      commit('setAcl', acl)
      return acl
    },

    // 获取用户拥有的service信息
    async GetServices ({ commit }) {
      const services = await authService.getServices()
      commit('setServices', services)
      return services
    },

    // 登出
    async Logout ({ commit }) {
      return new Promise((resolve) => {
        commit('SET_TOKEN', '')
        commit('SET_ROLES', [])
        commit('SET_PERMISSIONS', [])
        removeToken()
        resolve()
      })
    }
  }
}

export default user
