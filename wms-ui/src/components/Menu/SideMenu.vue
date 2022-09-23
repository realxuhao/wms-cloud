<template>
  <a-layout-sider
    v-model="collapsed"
    width="256px"
    :class="['sider', isDesktop() ? null : 'shadow', theme, fixSiderbar ? 'ant-fixed-sidemenu' : null ]"
    :collapsible="collapsible"
    :trigger="null">
    <Logo />
    <s-menu
      :collapsed="collapsed"
      :menu="menus"
      :theme="theme"
      :mode="mode"
      @select="onSelect"
      style="padding: 16px 0px;"></s-menu>

    <div class="logout">
      <div class="content" @click="handleLogout">
        <a-icon type="logout" />
        <span>退出登录</span>
      </div>
    </div>
  </a-layout-sider>

</template>

<script>
import Logo from '@/components/tools/Logo'
import SMenu from './index'
import { mixin, mixinDevice } from '@/utils/mixin'

export default {
  name: 'SideMenu',
  components: { Logo, SMenu },
  mixins: [mixin, mixinDevice],
  props: {
    mode: {
      type: String,
      required: false,
      default: 'inline'
    },
    theme: {
      type: String,
      required: false,
      default: 'dark'
    },
    collapsible: {
      type: Boolean,
      required: false,
      default: false
    },
    collapsed: {
      type: Boolean,
      required: false,
      default: false
    },
    menus: {
      type: Array,
      required: true
    }
  },
  methods: {
    onSelect (obj) {
      this.$emit('menuSelect', obj)
    },
    handleLogout () {
      this.$confirm({
        title: '提示',
        content: '真的要注销登录吗 ?',
        onOk: async () => {
          await this.$store.dispatch('Logout')
          setTimeout(() => {
            window.location.reload()
          }, 16)
        },
        onCancel () {
        }
      })
    }
  }
}
</script>

<style scoped lang="less">
.logout{
  position: absolute;
  bottom: 10px;
  color: #71747B;
  font-size: 14px;
  width: 100%;
  padding: 0px 24px;
  display: flex;
  cursor: pointer;
  &:hover{
    color: #fff;
    border-color: #fff;
  }
  .content{
    padding: 4px;
    border-radius: 6px;
    border: 1px solid transparent;
    span{
      margin-left: 8px;
    }
  }
}
</style>
