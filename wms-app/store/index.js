import Vue from 'vue'
import Vuex from 'vuex'

import user from './modules/user'
import materialIn from './modules/materialIn'
import binIn from './modules/binIn'
import plant from './modules/plant'
import picking from './modules/picking'
import receiveMaterial from './modules/receiveMaterial'
import kanban from './modules/kanban'

import getters from './getters'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    user,
	materialIn,
	binIn,
	plant,
	picking,
	receiveMaterial,
	kanban
  },
  state: {

  },
  mutations: {

  },
  actions: {

  },
  getters
})
