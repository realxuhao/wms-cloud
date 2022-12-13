<template>
	<view @click="handleGoto">
		跳转
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
					url:'/pages/wareTransshipment/shipmentOperation'
				})
			}
		}
	}
</script>

<style>

</style>
