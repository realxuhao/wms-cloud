import Vue from 'vue'
import Vuex from 'vuex'

import user from './modules/user'
import materialIn from './modules/materialIn'
import binIn from './modules/binIn'
import plant from './modules/plant'
import materialCall from './modules/materialCall'
import receiveMaterial from './modules/receiveMaterial'
import kanban from './modules/kanban'
import wareShift from './modules/wareShift'

import getters from './getters'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    user,
	materialIn,
	binIn,
	plant,
	materialCall,
	receiveMaterial,
	kanban,
	wareShift
  },
  state: {

  },
  mutations: {

  },
  actions: {

  },
  getters
})
