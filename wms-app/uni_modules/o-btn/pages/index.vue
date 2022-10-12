<template>
	<view class="p-2">
		<view class="py-2">
			<view class="text-bold pb-2">按钮尺寸</view>
			<o-btn size="xs">迷你按钮</o-btn>
			<o-btn size="sm">小按钮</o-btn>
			<o-btn loading>默认按钮</o-btn>
			<o-btn size="lg">大按钮</o-btn>
			<o-btn block size="sm">块级按钮sm</o-btn>
			<o-btn block :loading="loading" :loading-text="Load">块级按钮</o-btn>
			<o-btn block size="lg" icon="../../static/2.png" loading>块级按钮lg</o-btn>
		</view>
		<view class="py-2">
			<view class="text-bold pb-2">禁用状态</view>
			<o-btn disabled loading>默认</o-btn>
			<o-btn type="blue" disabled>蓝色禁用</o-btn>
			<o-btn class="my-2" type="yellow" disabled>黄色禁用</o-btn>
			<o-btn type="orange" plain disabled>橙色镂空禁用</o-btn>
		</view>
		<view class="py-2">
			<view class="text-bold pb-2">图标应用</view>
			<o-btn loading>Loading</o-btn>
			<o-btn type="blue" icon="../../static/2.png">图片图标</o-btn>
			<o-btn type="cyan" icon="icon-bofang">字体图标</o-btn>
			<o-btn type="orange" icon="icon-bofang" plain>橙色镂空字体图标</o-btn>
		</view>
		<view class="py-2">
			<view class="text-bold pb-2">镂空状态</view>
			<o-btn type="grey" plain>深灰</o-btn>
			<o-btn type="blue" plain>蓝色</o-btn>
			<o-btn type="cyan" plain>青色</o-btn>
		</view>
		<view class="py-2">
			<view class="text-bold pb-2">按钮形状</view>
			<o-btn type="blue">默认形状</o-btn>
			<o-btn type="blue" square>方形</o-btn>
			<o-btn type="blue" round>圆形</o-btn>
		</view>
		<view class="py-2">
			<view class="text-bold pb-2">按钮颜色</view>
			<o-btn type="red" loading>红色 Danger / Error</o-btn>
			<o-btn loading>默认 Default</o-btn>
			<o-btn type="green">绿色 Success</o-btn>
			<o-btn type="blue">蓝色 Primary</o-btn>
			<o-btn type="warning">黄色 Warning</o-btn>
			<o-btn type="orange">橙色</o-btn>
			<o-btn type="cyan">青色</o-btn>
			<o-btn type="gray">浅灰色</o-btn>
			<o-btn type="grey">深灰色</o-btn>
			<o-btn type="black">黑色</o-btn>
			<o-btn type="purple">紫色</o-btn>
			<o-btn type="brown">褐色</o-btn>
			<o-btn type="pink">粉色</o-btn>
			<o-btn type="white" >白色</o-btn>
		</view>
		

	</view>
</template>

<script>
	export default {
		data() {
			return {
				Load:'加载中…',
				loading:true,
			};
		},
		mounted() {
			setTimeout(()=> {
				this.Load='',
				this.loading=false
			}, 3000);
		}
	}
</script>

<style lang="scss">
	page{background-color:#f6f6f6;}
	.p-2{padding:16rpx;}
	.my-2{margin: 20rpx 0 !important;}
</style>
