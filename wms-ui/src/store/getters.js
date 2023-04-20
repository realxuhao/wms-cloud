import appConfig from '@/config/app.config'

const getters = {
  // app constants
  serviceName: () => appConfig.serviceName,
  tenant: () => appConfig.tenant,
  appName: () => appConfig.appName,
  footerText: () => appConfig.footerText,
  // app dynamic
  locale: state => state.app.locale,

  // user constants
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  nickname: state => state.user.name,
  welcome: state => state.user.welcome,
  roles: state => state.user.roles,
  permissions: state => state.user.permissions,
  userInfo: state => state.user.info,
  menus: state => state.permission.menus,
  routers: state => state.permission.routers,
  multiTab: state => state.app.multiTab,
  dict: state => state.dict.dict
}

export default getters
