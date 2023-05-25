<template>
  <div class="blank-window-wrapper">
    <router-view />
  </div>
</template>

<script>

export default {
  name: 'BlankLayout'
}
</script>

<style scoped>
.blank-window-wrapper{
  padding: 12px 0;
}

</style>
