<template>
  <a-config-provider :locale="antLocale">
    <div id="app">
      <div class="top-nav">
      </div>
      <router-view/>
    </div>
  </a-config-provider>
</template>

<script>
import { AppDeviceEnquire } from '@/utils/mixin'

export default {
  mixins: [AppDeviceEnquire],
  computed: {
    antLocale () {
      let locale = this.$i18n.antLocales[this.$store.getters.locale]
      locale = locale === undefined ? this.$i18n.antLocales[this.$i18n.defaultLocale] : locale
      return locale
    }
  },
  mounted () {
  }
}
</script>
<style>
  #app {
    height: 100%;
  }

  .top-nav{
    background: url(./assets/nutricia-color.png);
    background-repeat: no-repeat;
    width: 100%;
    height: 7px;
    position: fixed;
    z-index: 9999;
    margin-bottom: 20px;
  }
</style>
