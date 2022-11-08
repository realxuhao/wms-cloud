<template>
	<my-page nav-title="扫描SSCC码" >
			<view class="content" slot="page-main" @click="handleGotoCount">
				<image src="/static/sku-phone.png" class="m-b-8"></image>
				<text>请将激光扫描头对准SSCC码区域</text>
			</view>
			<Message ref="message"></Message>
			<ScanCode></ScanCode>
	</my-page>
</template>

<script>
	import Message from '@/components/Message'
	import ScanCode from '@/components/ScanCode'
	
	export default {
	components:{
		Message,
		ScanCode
	},
	 onLoad() {  
		   var _this = this  
		   uni.$on('scancodedate',function(data){  
			_this.code = data.code
			_this.checkBinIn(data.code)
			uni.$emit('stopScan')
		   })  
		},  
		onUnload() {  
		   // 移除监听事件      
		   uni.$off('scancodedate')
		},
		data() {
			return {
				code:'',
			};
		},
		methods:{
			async checkBinIn(barCode){
				try{
					const data = await this.$store.dispatch('binIn/getByMesBarCode',barCode)
					if(!data && data.status ===1 ){
						throw Error('已上架，请勿重复操作')
					}
					this.handleGotoOperation()
				}catch(e){
					this.$refs.message.error(e.message)
					uni.$emit('startScan')
				}
			},
			handleGotoOperation(){
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
			font-size: 12px;
		}
	}
</style>
