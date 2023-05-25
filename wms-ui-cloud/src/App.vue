<template>
  <a-config-provider :locale="locale">
    <div id="app">
      <div class="top-nav">
      </div>
      <router-view/>
    </div>
  </a-config-provider>
</template>

<script>
import { domTitle, setDocumentTitle } from '@/utils/domUtil'
import { i18nRender } from '@/locales'

export default {
  data () {
    return {
    }
  },
  computed: {
    locale () {
      // 只是为了切换语言时，更新标题
      const { title } = this.$route.meta
      title && (setDocumentTitle(`${i18nRender(title)} - ${domTitle}`))

      return this.$i18n.getLocaleMessage(this.$store.getters.lang).antLocale
    }
  }
}
</script>

<style scoped lang="less">
.top-nav{
    width: 100%;
    height: 4px; 
    position: fixed;
    z-index: 9999;
    margin-bottom: 20px;
    background: @primary-color;
  }
</style>