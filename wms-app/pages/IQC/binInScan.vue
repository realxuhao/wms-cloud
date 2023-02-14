<template>
	<my-page nav-title="扫描SSCC码" :shadow="false" :border="false">
			<view class="content" slot="page-main" >
				<image src="/static/sku-phone.png" class="m-b-8"></image>
				<text>请将激光扫描头对准SSCC码区域</text>
			</view>
			<Message ref="message"></Message>
	</my-page>
</template>

<script>
	import Bus from '@/utils/bus'
	import Message from '@/components/Message'
	
	export default {
		components:{
			Message,
		},
		data() {
			return {
				code:'',
			};
		},
		onShow(){
			Bus.$on('scancodedate',this.scanCodeCallback)
		},
		destroyed() {
			Bus.$off("scancodedate");
		},
		// mounted() {
		// 	this.checkBinIn('20230213669006391113695972103025192112271124000800')
		// },
		methods:{
			async scanCodeCallback(data){
				Bus.$emit('stopScan')
				this.code = data.code
				this.checkBinIn(data.code)
			},
			async checkBinIn(barCode){
				try{
					uni.showLoading()
					const data = await this.$store.dispatch('IQC/getSample',barCode)
					if(!data && data.status !== 2 ){
						throw Error('此托已上架或非IQC待上架托，请确认')
					}
					this.handleGotoOperation()
				}catch(e){
					this.$refs.message.error(e.message)
				}finally{
					uni.hideLoading()
					Bus.$emit('startScan')
				}
			},
			handleGotoOperation(){
				Bus.$off("scancodedate",this.scanCodeCallback);
				uni.navigateTo({
					url:`/pages/IQC/binInOperation?barCode=${this.code}`
				})
			}
		}
	}
</script>

<style lang="scss">
	.wrapper{
		display: flex;
		flex-direction: column;
	}
	
	.content{
		height: 100%;
		background-color: $primary-color;
		flex: 1;
		display: flex;
		align-items: center;
		// justify-content: center;
		flex-direction: column;
		image{
			width: 180px;
			// height: 160px;
			margin-top: 120px;
			margin-bottom: 32px;
		}
		text{
			color: #fff;
			font-size: 16px;
		}
	}
</style>
