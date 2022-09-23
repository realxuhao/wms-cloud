import _ from 'lodash'
import config from '@/config/app.config'
import { getAclList } from '@/utils/permissions'

const { needAuthorization } = config

function plugin (Vue) {
  if (plugin.installed) {
    return
  }

  !Vue.prototype.$auth && Object.defineProperties(Vue.prototype, {
    $auth: {
      get () {
        return (elAcl, options = {}) => {
          if (!needAuthorization) {
            return true
          }
          const { indirect = false } = options
          const acls = typeof elAcl === 'string' && (elAcl ? [elAcl] : []) || elAcl || []
          let res = true
          if (acls.length > 0) {
            const userAcl = this.$store.getters.userAcl
            const aclType = indirect ? 'indirect' : 'direct'
            const aclList = getAclList(userAcl, aclType)

            res = _.some(acls, acl => _.includes(aclList, acl))
          }
          return res
        }
      }
    }
  })
}

export default plugin
