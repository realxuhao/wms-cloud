<template>
  <view style="width: 100%; height: 100%;">
    <view class="tabs">
      <view v-for="(tab, index) in tabs" class="tab-item" :key="index" :class="current==index&&'active'" @click="current = index">
      {{tab.title}}
      </view>
    </view>
    <swiper :current="current" class="swiper-box" @change="onChange">
        <swiper-item v-for="(item ,index) in tabs" :key="index">
          <scroll-view scroll-y="true" style="height: 100%;" @scrolltolower="scrolltolower">
            <!-- #ifdef MP -->
            <slot name="{{'content' + index}}"></slot>    
            <!-- #endif -->
            <!-- #ifndef MP -->
            <slot :name="'content'+index"></slot>
            <!-- #endif -->
          </scroll-view>
        </swiper-item>
    </swiper>
  </view>
</template>

<script>
	export default {
    name: 'swiperTab',
    props: {
      currentTab: {
        type: Number,
        default: 0
      },
      tabs: {
        type: Array,
        default: []
      }
    },
		data() {
			return {
				current: this.currentTab
			};
		},
    methods: {
      onChange(e) {
        this.current = e.detail.current;
        this.$emit('change', e.detail.current);
      },
      scrolltolower(e) {
        this.$emit('onReachBottom', this.current);
        console.log(this.current)
      }
    }
	}
</script>
<style>
swiper-tab{
  display: block;
  height: 100%;
}
</style>
<style lang="scss" scoped>
.tabs{
  width: 750rpx;
  height: 75rpx;
  display: flex;
  .tab-item {
    flex: 1;
    text-align: center;
    color: grey;
    line-height: 73rpx;
    font-size: 28rpx;
    font-weight: 500;
    height: 100%;
    box-sizing: border-box;
    &.active{
      color: $uni-color-primary;
      border-bottom: 2px solid $uni-color-primary;
    }
  }
}
.swiper-box{
  width: 100%;
  overflow: auto;
}
</style>
