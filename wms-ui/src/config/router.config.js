// eslint-disable-next-line
import { UserLayout, BasicLayout, RouteView, BlankLayout, PageView } from '@/layouts'

export const setDefaultRouter = function () {
  return '/master-data'
}

export const asyncRouterMap = [
  {
    path: '/',
    name: 'index',
    component: BasicLayout,
    meta: { title: '首页' },
    redirect: '/master-data',
    children: [
      // 主数据
      {
        path: '/master-data',
        name: 'masterData',
        component: RouteView,
        redirect: '/master-data/material-type',
        meta: { title: '主数据', icon: 'hdd', permission: [] },
        children: [
          {
            path: '/master-data/material-type',
            name: 'materialType',
            component: () => import('@/views/masterData/materialType/Index'),
            meta: { title: '物料类型', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/master-data/material',
            name: 'material',
            component: () => import('@/views/masterData/material/Index'),
            meta: { title: '物料管理', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/master-data/material/dispatch-rule',
            name: 'materialDispatchRule',
            component: () => import('@/views/masterData/material/DispatchRule'),
            meta: { title: '物料上架规则', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/master-data/supplier',
            name: 'supplier',
            component: () => import('@/views/masterData/supplier/Index'),
            meta: { title: '供应商管理', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/master-data/move-type',
            name: 'moveType',
            component: () => import('@/views/masterData/moveType/Index'),
            meta: { title: '移动类型', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/master-data/time-window',
            name: 'timeWindow',
            component: () => import('@/views/masterData/timeWindow/Index'),
            meta: { title: '时间窗口', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/master-data/pallet',
            name: 'pallet',
            component: () => import('@/views/masterData/pallet/Index'),
            meta: { title: '托盘类型管理', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/master-data/ware',
            name: 'ware',
            component: () => import('@/views/masterData/ware/Index'),
            meta: { title: '仓库管理', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/master-data/area',
            name: 'area',
            component: () => import('@/views/masterData/area/Index'),
            meta: { title: '存储区管理', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/master-data/frame',
            name: 'frame',
            component: () => import('@/views/masterData/frame/Index'),
            meta: { title: '跨管理', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/master-data/bin',
            name: 'bin',
            component: () => import('@/views/masterData/bin/Index'),
            meta: { title: '库位管理', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/nmd/rule',
            name: 'nmdRule',
            component: () => import('@/views/masterData/nmd/NmdRule'),
            meta: { title: 'NMD抽样', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/ecn/rule',
            name: 'ecnRule',
            component: () => import('@/views/masterData/ecn/EcnRule.vue'),
            meta: { title: 'ECN抽样', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/fsmp/rule',
            name: 'fsmpRule',
            component: () => import('@/views/masterData/fsmp/FSMPRule.vue'),
            meta: { title: 'FSMP抽样', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/product/dispatch-rule',
            name: 'productDispatchRule',
            component: () => import('@/views/masterData/product/DispatchRule'),
            meta: { title: '成品上架规则', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/master-data/productPackaging',
            name: 'productPackaging',
            component: () => import('@/views/masterData/productPackaging/Index'),
            meta: { title: '成品包装量', permission: [] },
            hideChildrenInMenu: true
          }
        ]
      },
      {
        path: '/material-in',
        name: 'materialIn',
        component: RouteView,
        redirect: '/material-in/list',
        meta: { title: '原材料入库', icon: 'hdd', permission: [] },
        children: [
          {
            path: '/material-in/reveive/list',
            name: 'materialReveiveList',
            component: () => import('@/views/materialIn/MaterialReceiveList'),
            meta: { title: '收货清单', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/material-in/list',
            name: 'materialInList',
            component: () => import('@/views/materialIn/MaterialInList'),
            meta: { title: '入库清单', permission: [] },
            hideChildrenInMenu: true
          }
        ]
      },
      {
        path: '/bin-in',
        name: 'binIn',
        component: RouteView,
        redirect: '/bin-in/list',
        meta: { title: '原材料上架', icon: 'hdd', permission: [] },
        children: [
          {
            path: '/bin-in/list',
            name: 'binInList',
            component: () => import('@/views/binIn/BinIn'),
            meta: { title: '上架记录', permission: [] },
            hideChildrenInMenu: true
          }
        ]
      },
      {
        path: '/stock',
        name: 'stock',
        component: RouteView,
        redirect: '/bin-in/list',
        meta: { title: '库内管理', icon: 'hdd', permission: [] },
        children: [
          {
            path: '/stock/list',
            name: 'stockList',
            component: () => import('@/views/stock/Stock'),
            meta: { title: '库存查询', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/manual-trans/list',
            name: 'transList',
            component: () => import('@/views/manualTrans/transList'),
            meta: { title: '转储任务列表', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/split/list',
            name: 'splitList',
            component: () => import('@/views/split/SplitList'),
            meta: { title: '拆托记录', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/ware-shift/return-material',
            name: 'returnMaterial',
            component: () => import('@/views/wareShift/ReturnMaterial'),
            meta: { title: '退库记录', permission: [] },
            hideChildrenInMenu: true
          }
        ]
      },
      {
        path: '/feeding',
        name: 'stock',
        component: RouteView,
        redirect: '/feeding/list',
        meta: { title: '生产叫料管理', icon: 'hdd', permission: [] },
        children: [
          {
            path: '/feeding/list',
            name: 'feedingList',
            component: () => import('@/views/materialFeeding/MaterialFeeding'),
            meta: { title: '叫料记录', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/picking-order/list',
            name: 'pickingOrderList',
            component: () => import('@/views/materialFeeding/PickingOrder'),
            meta: { title: '拣配任务', permission: [] },
            hideChildrenInMenu: true
          }
        ]
      },
      {
        path: '/ware-shift',
        name: 'wareShift',
        component: RouteView,
        redirect: '/ware-shift/list',
        meta: { title: '移库管理', icon: 'hdd', permission: [] },
        children: [
          {
            path: '/ware-shift/list',
            name: 'shiftList',
            component: () => import('@/views/wareShift/shiftList'),
            meta: { title: '移库任务列表', permission: [] },
            hideChildrenInMenu: true
          }

        ]
      },
      {
        path: '/iqc',
        name: 'iqc',
        redirect: '/iqc/list',
        component: RouteView,
        meta: { title: 'IQC电子流', icon: 'hdd', permission: [] },
        children: [
          {
            path: '/iqc/list',
            name: 'iqcList',
            component: () => import('@/views/iqc/sample/IQCList.vue'),
            meta: { title: 'IQC抽样计划', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/iqc/quality',
            name: 'iqcQuality',
            component: () => import('@/views/iqc/quality/QualityManage.vue'),
            meta: { title: 'IQC质检管理', permission: [] },
            hideChildrenInMenu: true
          }
        ]
      },
      {
        path: '/vehicle-reservation',
        name: 'vehicleReservation',
        component: RouteView,
        redirect: '/vehicle-reservation/list',
        meta: { title: '车辆预约管理', icon: 'hdd', permission: [] },
        children: [
          {
            path: '/vehicle-reservation/purchase',
            name: 'purchase',
            component: () => import('@/views/vehicleReservation/purchase/Index'),
            meta: { title: '采购订单管理', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/vehicle-reservation/supplier',
            name: 'vr-supplier',
            component: () => import('@/views/vehicleReservation/supplier/Index'),
            meta: { title: '供应商预约', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/vehicle-reservation/dispatch',
            name: 'dispatch',
            component: () => import('@/views/vehicleReservation/dispatch/Index'),
            meta: { title: '仓库调度', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/vehicle-reservation/timeWindowBoard/ware',
            name: 'wareTimeWindowBoard-ware',
            component: () => import('@/views/vehicleReservation/timeWindowBoard/WareIndex'),
            meta: { title: '仓库大屏', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/vehicle-reservation/timeWindowBoard/guard',
            name: 'wareTimeWindowBoard-guard',
            component: () => import('@/views/vehicleReservation/timeWindowBoard/GuardIndex'),
            meta: { title: '门卫大屏', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/vehicle-reservation/signIn',
            name: 'signIn',
            component: () => import('@/views/vehicleReservation/signIn/Index'),
            meta: { title: '签到记录', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/vehicle-reservation/black-driver',
            name: 'blackDeriver',
            component: () => import('@/views/vehicleReservation/blackDriver/Index'),
            meta: { title: '司机名单', permission: [] },
            hideChildrenInMenu: true
          }
        ]
      },
      {
        path: '/finished-product',
        name: 'finishedProduct',
        redirect: '/finished-product/plan-list',
        component: RouteView,
        meta: { title: '成品管理', icon: 'hdd', permission: [] },
        children: [
          {
            path: '/finished-product/plan-list',
            name: 'finishedProductPalnList',
            component: () => import('@/views/finishedProduct/FinishedProductPlanList.vue'),
            meta: { title: '成品打包计划', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/finished-product/task-list',
            name: 'finishedProductTaskList',
            component: () => import('@/views/finishedProduct/FinishedProductTaskList.vue'),
            meta: { title: '成品打包任务', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/finished-product/packing-history-list',
            name: 'finishedProductPackingHistoryList',
            component: () => import('@/views/finishedProduct/FinishedProductPackingHistoryList.vue'),
            meta: { title: '成品打包记录', permission: [] },
            hideChildrenInMenu: true
          },
          // {
          //   path: '/finished-product/in',
          //   name: 'finishedProductIn',
          //   component: () => import('@/views/finishedProduct/FinishedProductIn.vue'),
          //   meta: { title: '成品入库', permission: [] },
          //   hideChildrenInMenu: true
          // },
          {
            path: '/finished-product/storage-list',
            name: 'finishedProductStorageList',
            component: () => import('@/views/finishedProduct/FinishedProductStorage.vue'),
            meta: { title: '成品入库任务', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/finished-product/inventory-list',
            name: 'finishedProductInventoryList',
            component: () => import('@/views/finishedProduct/FinishedProductInventory.vue'),
            meta: { title: '成品库存', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/finished-product/transfer-list',
            name: 'finishedProductTransferList',
            component: () => import('@/views/finishedProduct/FinishedProductTransfer.vue'),
            meta: { title: '成品移库', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/finished-product/quality-manage',
            name: 'finishedProductQualityManage',
            component: () => import('@/views/finishedProduct/FinishedProductQualityManage.vue'),
            meta: { title: '成品质检管理', permission: [] },
            hideChildrenInMenu: true
          }
        ]
      },
      {
        path: '/stock-take',
        name: 'stockTake',
        component: RouteView,
        redirect: '/stock-take/list',
        meta: { title: '盘点管理', icon: 'hdd', permission: [] },
        children: [
          {
            path: '/stock-take/list',
            name: 'stockTakeList',
            component: () => import('@/views/stockTake/StockTakePlan.vue'),
            meta: { title: '盘点计划列表', permission: [] },
            hideChildrenInMenu: true
          },
          {
            path: '/stock-take/stockTakePlanDetail',
            name: 'stockTakePlanDetail',
            component: () => import('@/views/stockTake/StockTakePlanDetail.vue'),
            meta: { title: '盘点明细列表', permission: [] },
            hideChildrenInMenu: true
          }
        ]
      },
      {
        path: '/comparison',
        name: 'comparison',
        redirect: '/comparison/list',
        component: RouteView,
        meta: { title: '库存对比', icon: 'hdd', permission: [] },
        children: [
          {
            path: '/comparison/list',
            name: 'comparisonList',
            component: () => import('@/views/comparison/Comparison.vue'),
            meta: { title: '库存对比列表', permission: [] },
            hideChildrenInMenu: true
          }

        ]
      }
    ]
  },

  {
    path: '/fullscreen',
    name: 'fullscreen',
    meta: {},
    component: BlankLayout,
    children: [
      {
        path: '/fullscreen/time-window',
        name: 'fullscreenTimeWIndow',
        meta: {},
        component: () => import('@/views/masterData/timeWindow/Index'),
        props () {
          return {
            isFullscreen: true
          }
        }
      },
      {
        path: '/fullscreen/packing-history-list',
        name: 'fullscreenPackingHistoryList',
        meta: {},
        component: () => import('@/views/finishedProduct/FinishedProductTaskList'),
        props () {
          return {
            isFullscreen: true
          }
        }
      }
    ]
  },
  {
    path: '*', redirect: '/404', hidden: true
  }
]

/**
 * 基础路由
 * @type { *[] }
 */
export const constantRouterMap = [
  {
    path: '/user',
    name: 'user',
    component: UserLayout,
    redirect: { name: 'login' },
    children: [
      {
        path: 'login',
        name: 'login',
        component: () => import('@/views/login/Login')
      }
    ]
  },
  {
    path: '/404',
    component: () => import(/* webpackChunkName: "fail" */ '@/views/exception/404')
  }
]
