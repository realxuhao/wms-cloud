<template>
  <div :class="wrpCls">
    <a-space>
      <a-select
        mode="multiple"
        :default-value="['7751W1', '7751W2']"
        style="width: 300px;"
        placeholder="请选择仓库"
      >
        <a-select-option value="all">
          All
        </a-select-option>
        <a-select-option value="7751W1">
          7751W1
        </a-select-option>
        <a-select-option value="7751W2">
          7751W2
        </a-select-option>
      </a-select>
      <avatar-dropdown :menu="showMenu" :current-user="currentUser" :class="prefixCls" />
     
    </a-space>
  </div>
</template>

<script>
import AvatarDropdown from './AvatarDropdown'
import NoticeIcon from '@/components/NoticeIcon'
import Screenfull from '@/components/Screenfull'
import SelectLang from '@/components/SelectLang'

export default {
  name: 'RightContent',
  components: {
    AvatarDropdown,
    NoticeIcon,
    Screenfull,
    SelectLang
  },
  props: {
    prefixCls: {
      type: String,
      default: 'ant-pro-global-header-index-action'
    },
    isMobile: {
      type: Boolean,
      default: () => false
    },
    topMenu: {
      type: Boolean,
      required: true
    },
    theme: {
      type: String,
      required: true
    }
  },
  data () {
    return {
      showMenu: true,
      currentUser: {},
      docUrl: 'https://docs.geekera.cn/RuoYi-Antdv/',
      githubUrl: 'https://github.com/fuzui/RuoYi-Antdv'
    }
  },
  computed: {
    wrpCls () {
      return {
        'ant-pro-global-header-index-right': true,
        [`ant-pro-global-header-index-${(this.isMobile || !this.topMenu) ? 'light' : this.theme}`]: true
      }
    }
  },
  mounted () {
    setTimeout(() => {
      this.currentUser = {
        name: 'RuoYi'
      }
    }, 1500)
  },
  methods: {
    toDoc () {
      window.open(this.docUrl)
    },
    toGithub () {
      window.open(this.githubUrl)
    }
  }
}
</script>
