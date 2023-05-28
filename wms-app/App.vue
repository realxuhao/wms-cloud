<script>
export default {
	onLaunch: async function() {
		console.log('App Launch');
		console.log('onload');
		try {
			await this.$store.dispatch('GetInfo');
		} catch (e) {
			uni.reLaunch({
				url: '/pages/login/index'
			});
			//TODO handle the exception
		} finally {
		}
	},
	onShow: function() {
		console.log('App Show');
	},
	onHide: function() {
		console.log('App Hide');
	},
	onLoad() {}
};
</script>

<style lang="scss">
/*每个页面公共css */
@import './uni.scss';
@import './global.scss';
@import 'static/icon/iconfont.css';
</style>
