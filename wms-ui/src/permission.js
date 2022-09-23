import notification from 'ant-design-vue/es/notification'
// import _ from 'lodash'
import { getToken } from '@/utils/cookie'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar custom style
// import { CustomError } from '@/utils/error'

import config from '@/config/app.config'

import router from './router'
import store from './store'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['login'] // no redirect whitelist

function redirectToLogin (redirectPath) {
  router.replace({ name: 'login', query: redirectPath })
}

async function loadGlobalData () {
}

function handleLoadError (error) {
  console.error(error)
  switch (error.type) {
    case 'noService': {
      window.location.href = config.homepageRootUrl
      break
    }
    default: {
      notification.error({
        message: '错误',
        description: '请求用户信息失败，请重试'
      })
      // setTimeout(() => { removeToken(); redirectToLogin() }, 5000)
    }
  }
}

function gotoNext (to, from, next, redirect) {
  const { meta } = to
  let permission = true

  if (meta && meta.hook) {
    // hook用于判断动态路由权限
    permission = meta.hook(to.params, meta)
  }

  if (!permission) {
    router.push({ path: '/404' })
  } else if (redirect && to.path === redirect) {
    // hack方法 确保addRoutes已完成 ,set the replace: true so the navigation will not leave a history record
    next({ ...to, replace: true })
  } else if (redirect) {
    // 跳转到目的路由
    next({ path: redirect })
  } else {
    next()
  }
}

async function loginCallback (to, from, next) {
  let redirect = null
  if (store.getters.userLoaded === false) {
    try {
      const [userInfo] = await Promise.all([
        store.dispatch('GetInfo')
      ])

      store.commit('setLoadingStatus', true)

      const isAdmin = userInfo.isSuperTenant || userInfo.accountId === userInfo.tenantCreatorId
      const addRouters = await store.dispatch('GenerateRoutes', isAdmin)

      // 动态添加可访问路由表
      router.addRoutes(addRouters)

      await loadGlobalData()

      redirect = decodeURIComponent(from.query.redirect || to.path)
    } catch (error) {
      handleLoadError(error)
      return
    }
  }
  gotoNext(to, from, next, redirect)
}

const checkAuthentication = async (to, from, next) => {
  NProgress.start() // start progress bar
  // to.meta && (typeof to.meta.title !== 'undefined' && setDocumentTitle(`${to.meta.title} - ${domTitle}`))
  const token = getToken()
  if (token) {
    /* has token */
    NProgress.done()
    await loginCallback(to, from, next)
  } else {
    if (whiteList.includes(to.name)) {
      // 在免登录白名单，直接进入
      next()
    } else {
      const { path } = to
      redirectToLogin({ redirectPath: path })
      NProgress.done() // if current page is login will not trigger afterEach hook, so manually handle it
    }
  }
}

router.beforeEach(checkAuthentication)

router.afterEach(() => {
  NProgress.done() // finish progress bar
})
