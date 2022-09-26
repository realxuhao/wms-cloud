// import Vue from 'vue'
import { mapState } from 'vuex'
import { deviceEnquire, DEVICE_TYPE } from '@/utils/device'

// const mixinComputed = Vue.config.optionMergeStrategies.computed
// const mixinMethods = Vue.config.optionMergeStrategies.methods

export const mixin = {
  computed: {
    ...mapState({
      layoutMode: state => state.app.layout,
      navTheme: state => state.app.theme,
      primaryColor: state => state.app.color,
      colorWeak: state => state.app.weak,
      fixedHeader: state => state.app.fixedHeader,
      fixSiderbar: state => state.app.fixSiderbar,
      fixSidebar: state => state.app.fixSiderbar,
      contentWidth: state => state.app.contentWidth,
      autoHideHeader: state => state.app.autoHideHeader,
      sidebarOpened: state => state.app.sidebar,
      multiTab: state => state.app.multiTab
    })
  },
  methods: {
    isTopMenu () {
      return this.layoutMode === 'topmenu'
    },
    isSideMenu () {
      return !this.isTopMenu()
    }
  }
}

export const AppDeviceEnquire = {
  mounted () {
    const { $store } = this
    deviceEnquire(deviceType => {
      switch (deviceType) {
        case DEVICE_TYPE.DESKTOP:
          $store.commit('TOGGLE_DEVICE', 'desktop')
          $store.dispatch('setSidebar', true)
          break
        case DEVICE_TYPE.TABLET:
          $store.commit('TOGGLE_DEVICE', 'tablet')
          $store.dispatch('setSidebar', false)
          break
        case DEVICE_TYPE.MOBILE:
        default:
          $store.commit('TOGGLE_DEVICE', 'mobile')
          $store.dispatch('setSidebar', true)
          break
      }
    })
  }
}

export * from './mixinDevice'
export * from './mixinNotification'
export * from './mixinEvent'
export * from './mixinTableList'
