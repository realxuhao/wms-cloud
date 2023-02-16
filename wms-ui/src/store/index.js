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
import nmdRule from './modules/nmdRule'
import timeWindow from './modules/timeWindow'
import pallet from './modules/pallet'
import area from './modules/area'
import ware from './modules/ware'
import frame from './modules/frame'
import iqcSample from './modules/iqcSample'
import bin from './modules/bin'
import file from './modules/file'
import binIn from './modules/binIn'
import stock from './modules/stock'
import wareShift from './modules/wareShift'
import manualTrans from './modules/manualTrans'

import materialInList from './modules/materialInList'
import materialFeeding from './modules/materialFeeding'

import blackDriver from './modules/blackDriver'
import purchase from './modules/purchase'
import supplierReserve from './modules/supplierReserve'

import split from './modules/split'

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
    nmdRule,
    moveType,
    timeWindow,
    pallet,
    area,
    ware,
    frame,
    iqcSample,
    bin,
    stock,
    materialFeeding,

    blackDriver,
    purchase,
    supplierReserve,

    split,

    file,
    wareShift,
    manualTrans,
    materialInList,
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
