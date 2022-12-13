<template>
	<view @click="handleGoto">
		去扫描页面
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
			}
		},
		methods: {
			handleGoto(){
				uni.navigateTo({
					url:'/pages/wareTransshipment/receivingScan'
				})
			}
		}
	}
</script>

<style>

</style>
