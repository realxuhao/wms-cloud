<template>
	<view class="version">当前app版本：{{ version }}</view>
</template>

<script>
export default {
	name: 'app-version',
	data() {
		return {
			version: ''
		};
	},
	mounted() {
		plus.runtime.getProperty(plus.runtime.appid, wgtinfo => {
			this.version = wgtinfo.version; //客户端版本号
		});
	}
};
</script>

<style scoped lang="scss">
.version {
	padding: 8px 0;
	box-sizing: border-box;
	display: flex;
	justify-content: center;
}
</style>
