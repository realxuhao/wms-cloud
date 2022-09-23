<template>
  <div id="userLayout" :class="['user-layout-wrapper']">
    <div class="container">
      <router-view />
    </div>
  </div>
</template>

<script>

export default {

  name: 'UserLayout',

  mounted () {
    document.body.classList.add('userLayout')
  },

  beforeDestroy () {
    document.body.classList.remove('userLayout')
  }

}

</script>

<style lang="less" scoped>

#userLayout.user-layout-wrapper {

    height: 100%;

    .container {

      width: 100%;

      min-height: 100%;

      background: #f0f2f5 url(~@/assets/images/login/background.jpg) no-repeat 50%;

      background-size: 100%;

      // padding: 110px 0 144px;

      position: relative;

    }

  }

</style>
