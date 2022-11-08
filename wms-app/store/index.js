import Vue from 'vue'
import Vuex from 'vuex'

import user from './modules/user'
import materialIn from './modules/materialIn'
import binIn from './modules/binIn'

import getters from './getters'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    user,
	materialIn,
	binIn
  },
  state: {

  },
  mutations: {

  },
  actions: {

  },
  getters
})
