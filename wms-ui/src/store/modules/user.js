import { authService } from '@/api'
import _ from 'lodash'
import { removeToken } from 'minerva-ui-sdk'

const user = {
  state: {
    name: '',
    info: {
      accountDisplayName: ''
    },
    isLoaded: false,
    services: [],

    acl: {
      direct: [ ],
      indirect: [ ]
    }
  },

  mutations: {
    setUser (state, { name }) {
      state.name = name
    },
    setInfo (state, info) {
      state.info = info
    },
    setAcl (state, acl) {
      state.acl = acl
    },

    setLoadingStatus (state, status) {
      state.isLoaded = status
    },
    setServices (state, services) {
      state.services = _.filter(services, service => service.isEnable === true)
    }
  },

  actions: {
    async GetCodeImg () {
      const data = await authService.getCodeImg()
      return data
    },

    async Login ({ commit }, parameter) {
      const data = await authService.login(parameter)
      return data.data.access_token
    },

    // 获取用户信息
    async GetInfo ({ commit }) {
      const userInfo = await authService.getInfo()
      commit('setUser', { name: userInfo.accountDisplayName })
      commit('setInfo', userInfo)
      return userInfo
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
        removeToken()
        resolve()
      })
    }
  }
}

export default user
