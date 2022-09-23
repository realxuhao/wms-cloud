import Vue from 'vue'
import store from '@/store'
import { getAclList } from '@/utils/permissions'
import config from '@/config/app.config'

const { needAuthorization } = config

/**
 * Authorization 权限指令
 * 指令用法：
 *  - 在需要控制 authorization 级别权限的组件上使用 v-auth:[acl].[indirect].[disable], 如下：
 *    <a-button v-auth:[acl].direct.disable="delete">删除用户</a-button>
 *
 *  - 当前用户没有直接权限时，组件上使用了该指令则默认隐藏
 *  - 添加修饰符 indirect，则检查间接权限
 *  - 添加修饰符 disable，显示该组件，但是是不可用的状态
 */
const authorization = Vue.directive('auth', {
  inserted: function (el, binding, vNode) {
    if (!needAuthorization) {
      return
    }
    const { arg: acl, modifiers } = binding
    const userAcl = store.getters.userAcl
    const aclType = modifiers.indirect ? 'indirect' : 'direct'
    const showType = modifiers.disable ? 'disable' : 'hidden'
    const aclList = getAclList(userAcl, aclType)
    if (!aclList.includes(acl)) {
      if (showType === 'hidden') {
        el.parentNode && el.parentNode.removeChild(el) || (el.style.display = 'none')
      } else {
        // Vue.set(vNode.componentInstance, 'disabled', true)
        el.disabled = true
      }
    }
  }
})

export default authorization
