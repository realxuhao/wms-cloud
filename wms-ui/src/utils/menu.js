import _ from 'lodash'
import config from '@/config/app.config'

const { needAuthorization } = config

export function convertRouterToMenu (route) {
  if (!_.isArray(route.children) || route.children.length === 0) {
    return null
  }
  const subMenus = _.map(route.children, subRoute => ({
    path: subRoute.path,
    name: subRoute.name,
    title: subRoute.meta.title,
    hiddenInMenu: subRoute.meta.hiddenInMenu,
    subMenus: convertRouterToMenu(subRoute)
  }))
  return subMenus
}

export function hiddenRouterByHook (route, options) {
  const subMenus = _.map(route.children, subRoute => {
    const { meta = {} } = subRoute
    const { hiddenInMenu, hook } = meta
    const permission = hook && hook(options, meta)
    return {
      ...hiddenRouterByHook(subRoute, options),
      meta: { ...subRoute.meta, hiddenInMenu: !permission || hiddenInMenu }
    }
  })
  return {
    ...route,
    children: subMenus
  }
}

export function convertRouterToMenuWithHook (route, options) {
  route = needAuthorization ? hiddenRouterByHook(route, options) : route
  const menus = convertRouterToMenu(route)
  return menus
}
