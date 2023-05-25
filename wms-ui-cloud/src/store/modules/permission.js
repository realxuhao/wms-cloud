import _ from 'lodash'
import {
  setDefaultRouter,
  asyncRouterMap,
  constantRouterMap
} from '@/config/router.config'

function hasAcl (route, isAdmin) {
  if (route.meta && route.meta.isAdmin) {
    if (!isAdmin) {
      return false
    }
  }
  return true
}

function filterAsyncRouterByAcl (routerMap, isAdmin) {
  const accessedRouters = routerMap.filter(route => {
    if (hasAcl(route, isAdmin)) {
      if (route.children && route.children.length) {
        route.children = filterAsyncRouterByAcl(route.children, isAdmin)
      }
      return true
    }
    return false
  })

  return accessedRouters
}

function _generateRouterMap (router, routerMap) {
  if (!(router.children && router.children.length)) {
    return
  }
  _.each(router.children, subRouter => {
    _generateRouterMap(subRouter, routerMap)
    if (subRouter.name) {
      routerMap[subRouter.name] = subRouter
    }
  })
}

function generateRouterMap (routers) {
  const addRouterMap = {}
  _generateRouterMap({ children: routers }, addRouterMap)
  return addRouterMap
}

function setRoutersHook (routerMap, hook) {
  const routersWithHook = _.map(routerMap, route => {
    if (hook) {
      route.meta = {
        ...route.meta,
        hook
      }
    }
    if (route.children && route.children.length) {
      route.children = setRoutersHook(route.children, route.meta && route.meta.hook)
    }
    return route
  })
  return routersWithHook
}

const permission = {
  state: {
    routers: constantRouterMap,
    addRouters: asyncRouterMap,
    addRouterMap: {}
  },
  mutations: {
    setRouters: (state, routers) => {
      state.addRouters = routers
      state.routers = constantRouterMap.concat(routers)
      state.addRouterMap = generateRouterMap(routers)
    }
  },
  actions: {
    async GenerateRoutes ({ commit, state }, isAdmin) {
      const defaultRouterIndex = asyncRouterMap.findIndex(x => x.path === '/')
      if (defaultRouterIndex !== -1) {
        asyncRouterMap[defaultRouterIndex].redirect = setDefaultRouter(isAdmin)
      }

      const accessedRouters = await filterAsyncRouterByAcl(asyncRouterMap, isAdmin)
      const routersWithHook = await setRoutersHook(accessedRouters)

      commit('setRouters', routersWithHook)
      return state.addRouters
    }
  }
}

export default permission
