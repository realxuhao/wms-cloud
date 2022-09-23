<script>
import _ from 'lodash'
import MenuHeader from './MenuHeader'

export default {
  name: 'AppSider',
  components: {
    MenuHeader
  },
  props: {
    device: {
      type: String,
      required: false,
      default: 'desktop'
    },
    menus: {
      type: Array,
      required: true
    },
    to: {
      type: Object,
      required: true
    },
    title: {
      type: String,
      required: true
    }
  },
  data () {
    return {
      openKeys: [],
      selectedKeys: []
    }
  },
  computed: {
    rootSubmenuKeys () {
      const submenuKeys = _.map(_.filter(this.menus, menu => menu.subMenus), menu => menu.path)
      return submenuKeys
    }
  },
  methods: {
    onOpenChange (openKeys) {
      this.openKeys = openKeys
    },
    updateMenu () {
      const routes = this.$route.matched.concat()
      this.selectedKeys = [ routes.pop().path ]
    },
    renderMenu (menu) {
      if (menu.hiddenInMenu) {
        return null
      }
      if (menu.subMenus) {
        const subMenus = _.map(menu.subMenus, subMenu => {
          return this.renderMenu(subMenu)
        })
        return (
          <a-sub-menu key={menu.path}>
            <span slot="title">{menu.title}</span>
            {subMenus}
          </a-sub-menu>
        )
      }
      return (
        <a-menu-item key={menu.path}>
          <router-link to={{ name: menu.name }}>
            {menu.title}
          </router-link>
        </a-menu-item>
      )
    }
  },
  watch: {
    $route (value) {
      this.updateMenu()
    }
  },
  created () {
    this.openKeys = this.rootSubmenuKeys
    this.updateMenu()
  },
  render () {
    const menus = _.map(this.menus, menu => this.renderMenu(menu))
    return (
      <div>
        <MenuHeader title={this.title} to={this.to}></MenuHeader>
        <a-menu
          type="inner"
          mode={this.device === 'mobile' ? 'horizontal' : 'inline'}
          style={{ border: '0', width: this.device === 'mobile' ? '560px' : 'auto' }}
          selectedKeys={this.selectedKeys}
          openKeys={this.openKeys}
          onOpenChange={this.onOpenChange}
        >
          {menus}
        </a-menu>
      </div>
    )
  }
}
</script>
