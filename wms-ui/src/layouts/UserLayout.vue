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
    background-image: url(~@/assets/images/login/background.jpg);
    background-origin: 50%;
    background-size: 100%;
    width: 100%;

    .container {
      width: 100%;
      min-height: 100%;
      display: flex;
      justify-content: center;
      background: rgba(0,0,0,0.3);
    }

  }

</style>
