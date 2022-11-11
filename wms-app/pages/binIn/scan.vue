<template>
	<my-page nav-title="扫描SSCC码" >
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
			console.log('show')
			Bus.$on('scancodedate',this.scanCodeCallback)
		},
		destroyed() {
			Bus.$off("scancodedate");
		},
		methods:{
			async scanCodeCallback(data){
				this.code = data.code
				await this.checkBinIn(data.code)
				Bus.$emit('stopScan')
			},
			async checkBinIn(barCode){
				try{
					const data = await this.$store.dispatch('binIn/getByMesBarCode',barCode)
					if(!data && data.status ===1 ){
						throw Error('已上架，请勿重复操作')
					}
					this.handleGotoOperation()
				}catch(e){
					this.$refs.message.error(e.message)
					Bus.$emit('startScan')
				}
			},
			handleGotoOperation(){
				Bus.$off("scancodedate",this.scanCodeCallback);
				uni.navigateTo({
					url:`/pages/binIn/operation?barCode=${this.code}`
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
	/deep/.uni-navbar--shadow{
		box-shadow: none;
	}
	/deep/.uni-navbar--border{
		border: none;
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
