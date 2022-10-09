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
        redirect: '/master-data/material',
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
            meta: { title: '物料主数据管理', permission: [] },
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
          }
        ]
      },

      // account
      {
        path: '/account',
        component: RouteView,
        redirect: '/account/settings',
        name: 'account',
        meta: { title: '个人页', icon: 'user', keepAlive: true, permission: [] },
        children: [
          {
            path: '/account/settings',
            name: 'settings',
            component: () => import('@/views/account/settings/Index'),
            meta: { title: '个人设置', hideHeader: true, permission: [] },
            redirect: '/account/settings/base',
            hideChildrenInMenu: true,
            children: [
              {
                path: '/account/settings/base',
                name: 'BaseSettings',
                component: () => import('@/views/account/settings/BaseSetting'),
                meta: { title: '基本设置', permission: [] }
              },
              {
                path: '/account/settings/security',
                name: 'SecuritySettings',
                component: () => import('@/views/account/settings/Security'),
                meta: { title: '安全设置', keepAlive: true, permission: [] }
              },
              {
                path: '/account/settings/binding',
                name: 'BindingSettings',
                component: () => import('@/views/account/settings/Binding'),
                meta: { title: '账户绑定', keepAlive: true, permission: [] }
              },
              {
                path: '/account/settings/notification',
                name: 'NotificationSettings',
                component: () => import('@/views/account/settings/Notification'),
                meta: { title: '新消息通知', keepAlive: true, permission: [] }
              }
            ]
          }
        ]
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
