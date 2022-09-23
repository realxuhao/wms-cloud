<template>
  <div class="menu-header clearfix">
    <div class="menu-header-left">{{ title }}</div>
    <div class="menu-header-right" @click="onListClick"><a-icon type="bars"></a-icon></div>
  </div>
</template>
<script>
export default {
  name: 'MenuHeader',
  props: {
    title: {
      type: String,
      required: true
    },
    to: {
      type: Object,
      required: true
    }
  },
  methods: {
    onListClick () {
      this.$router.push(this.to)
    }
  }
}
</script>

<style lang="less" scoped>
.menu-header {
  padding: 15px 15px;
  font-size: 20px;

  .menu-header-left {
    max-width: 173px;
    overflow: hidden;
    float: left;
  }

  .menu-header-right {
    float: right;
    cursor: pointer;
  }
}

</style>
