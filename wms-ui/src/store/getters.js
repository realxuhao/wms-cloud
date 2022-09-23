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

  // user dynamic
  avatar: state => '',
  nickname: state => 'nickname',
  userAcl: state => state.user.acl,
  userInfo: state => state.user.info,
  userLoaded: state => state.user.isLoaded,
  userServices: state => state.user.services,

  addRouters: state => state.permission.addRouters
}

export default getters
