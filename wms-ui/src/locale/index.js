import _ from 'lodash'

import Vue from 'vue'
import VueI18n from 'vue-i18n'
import moment from 'moment'

// i18n locales
import enUS from './lang/en_US'
import zhCN from './lang/zh_CN'

// ant i18n locales
import antEnUS from 'ant-design-vue/lib/locale-provider/en_US'
import antZhCN from 'ant-design-vue/lib/locale-provider/zh_CN'

// moment i18n locales
import 'moment/locale/zh-cn'

// web configuration
import config from '@/config/defaultSettings'

function resolve (config, field) {
  const x = _.map(config, (value, key) => [key, value[field]])
  const y = _.unzip(x)
  const z = _.zipObject.apply(_, y)
  return z
}

const localeConfig = {
  'en-US': {
    enabled: true,
    message: { ...enUS },
    ant: antEnUS
  },
  'zh-CN': {
    enabled: true,
    message: { ...zhCN },
    ant: antZhCN
  }
}

// const timezoneConfig = {
//   'Asia/Shanghai': {
//     enabled: true
//   },
//   'UTC': {
//     enabled: true
//   },
//   'US/Hawaii': {
//     enabled: true
//   },
//   'US/Samoa': {
//     enabled: true
//   }
// }

const supportLocales = resolve(localeConfig, 'enabled')
const messages = resolve(localeConfig, 'message')
const antLocales = resolve(localeConfig, 'ant')

// const supportTimezones = resolve(timezoneConfig, 'enabled')

// Currently only support Simplified Chinese
const defaultLocale = supportLocales[config.defaultLocale] === true ? config.defaultLocale : 'zh-CN'
// const defaultTimezone = supportTimezones[config.defaultTimezone] === true ? config.defaultTimezone : 'Asia/Shanghai'

Vue.use(VueI18n)

moment.locale(defaultLocale)
// moment.tz.setDefault(defaultTimezone)

const i18n = new VueI18n({
  locale: defaultLocale,
  messages
})

i18n.defaultLocale = defaultLocale
i18n.supportLocales = supportLocales
i18n.antLocales = antLocales

// i18n.defaultTimezone = defaultTimezone
// i18n.supportTimezones = supportTimezones

export default i18n
