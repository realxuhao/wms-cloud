import Vue from 'vue'
import Vuex from 'vuex'

import app from './modules/app'
import user from './modules/user'

// default router permission control
import permission from './modules/permission'

// master data
import material from './modules/material'
import department from './modules/department'
import materialType from './modules/materialType'
import supplier from './modules/supplier'
import moveType from './modules/moveType'
import timeWindow from './modules/timeWindow'
import pallet from './modules/pallet'

// dynamic router permission control (Experimental)
// import permission from './modules/async-router'
import getters from './getters'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    app,
    user,
    permission,

    material,
    materialType,
    department,
    supplier,
    moveType,
    timeWindow,
    pallet
  },
  state: {

  },
  mutations: {

  },
  actions: {

  },
  getters
})