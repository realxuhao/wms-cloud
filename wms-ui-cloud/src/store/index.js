import Vue from 'vue'
import Vuex from 'vuex'

import app from './modules/app'
import user from './modules/user'
import tagsView from './modules/tagsView'
import dict from './modules/dict'

// default router permission control
// import permission from './modules/permission'
import permission from './modules/async-router'

import material from './modules/material'
import department from './modules/department'
import materialType from './modules/materialType'
import supplier from './modules/supplier'
import moveType from './modules/moveType'
import nmdRule from './modules/nmdRule'
import ecnRule from './modules/ecnRule'
import timeWindow from './modules/timeWindow'
import pallet from './modules/pallet'
import area from './modules/area'
import ware from './modules/ware'
import frame from './modules/frame'
import iqcSample from './modules/iqcSample'
import iqcManagement from './modules/iqcManagement'
import bin from './modules/bin'
import file from './modules/file'
import binIn from './modules/binIn'
import stock from './modules/stock'
import wareShift from './modules/wareShift'
import manualTrans from './modules/manualTrans'
import fsmp from './modules/fsmpRule'

import materialInList from './modules/materialInList'
import materialFeeding from './modules/materialFeeding'

import blackDriver from './modules/blackDriver'
import purchase from './modules/purchase'
import supplierReserve from './modules/supplierReserve'
import driverDeliver from './modules/driverDeliver'
import driverDispatch from './modules/driverDispatch'

import split from './modules/split'
import finishedProduct from './modules/finishedProduct'
import product from './modules/product'
import finishedProductStorage from './modules/finishedProductStorage'
import finishedProductInventory from './modules/finishedProductInventory'
import finishedProductTransfer from './modules/finishedProductTransfer'
import finishedProductQualityManage from './modules/finishedProductQualityManage'
import productPackaging from './modules/productPackaging'
import stockTake from './modules/stockTake'
import stockTakeDetail from './modules/stockTakeDetail'
import comparison from './modules/comparison'

// dashboard
import dashboard from './modules/dashboard'


// dynamic router permission control (Experimental)
// import permission from './modules/async-router'
import getters from './getters'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    app,
    user,
    tagsView,
    dict,
    permission,

    material,
    materialType,
    department,
    supplier,
    nmdRule,
    ecnRule,
    fsmp,
    moveType,
    timeWindow,
    pallet,
    area,
    ware,
    frame,
    iqcSample,
    iqcManagement,
    bin,
    stock,
    materialFeeding,

    blackDriver,
    purchase,
    supplierReserve,
    driverDeliver,
    driverDispatch,

    split,

    file,
    wareShift,
    manualTrans,
    materialInList,
    binIn,
    finishedProduct,
    product,
    finishedProductStorage,
    finishedProductInventory,
    finishedProductTransfer,
    finishedProductQualityManage,
    productPackaging,
    stockTake,
    stockTakeDetail,
    comparison,

    dashboard
  },
  state: {

  },
  mutations: {

  },
  actions: {

  },
  getters
})
