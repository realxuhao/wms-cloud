<template>
  <!-- <a-card :bordered="false" :bodyStyle="{ padding: '16px 0', height: '100%' }" :style="{ height: '100%' }"> -->
  <div class="m-content-main" :class="device">
    <div class="m-content-left">
      <AppSider :device="device" :title="title" :menus="menus" :to="backPath"></AppSider>
    </div>
    <div class="m-content-right">
      <RouteView></RouteView>
    </div>
  </div>
  <!-- </a-card> -->
</template>

<script>
import RouteView from './RouteView'
import AppSider from '@/components/Menu/AppSider'

export default {
  name: 'ManagementLayout',
  components: {
    AppSider,
    RouteView
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
    title: {
      type: String,
      required: true
    },
    backPath: {
      type: Object,
      required: true
    }
  }
}
</script>

<style lang="less" scoped>
.m-content-main {
  width: 100%;
  height: 100%;
  display: flex;
  overflow: auto;
  position: relative;

  .m-content-left {
    border-right: 1px solid #e8e8e8;
    flex: 0 0 224px;
    width: 224px;
    min-width: 224px;
    max-width: 224px;
    position: absolute;
    height: 100%;
    overflow-x: hidden;
    overflow-y: auto;
  }

  .m-content-right {
    width: calc(100% - 224px);
    flex: 1 1 auto;
    padding: 0 20px;
    margin-left: 224px;
    height: 100%;
    overflow-x: hidden;
    overflow-y: auto;
  }

  &.mobile {
    display: block;

    .m-content-left {
      border-right: unset;
      border-bottom: 1px solid #e8e8e8;
      width: 100%;
      min-width: 100%;
      max-width: 100%;
      height: 109px;
      overflow-x: auto;
      overflow-y: scroll;
    }
    .m-content-right {
      width: 100%;
      padding: 10px 10px;
      margin-top: 109px;
      margin-left: 0px;
      height: auto;
    }
  }
}
</style>
