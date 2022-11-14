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
	import Message from '@/components/Message'
	import Bus from '@/utils/bus'
	
	export default {
		components:{
			Message,
		},
		onShow(){
			Bus.$on('scancodedate',this.scanCodeCallback)
		},
		destroyed() {
			Bus.$off("scancodedate");
		},
		data() {
			return {
				code:'',
			};
		},
		methods:{
			async scanCodeCallback(data){
				this.code = data.code
				await this.checkMaterialIn(data.code)
				Bus.$emit('stopScan')
			},
			async checkMaterialIn(barCode){
				try{
					await this.$store.dispatch('materialIn/getAndCheckMaterialIn',barCode)
					
					this.handleGoto()
				}catch(e){
					if(e.code === 601){
						this.$refs.message.error('该批次原材料已检验')
					}else if(e.code === 602){
						this.$refs.message.error('该原材料已入库，请勿重复操作！')
					}else{
						this.$refs.message.error(e.message)
					}
				}
			},
			async handleMaterialIn(){
				try{
					await this.$store.dispatch('material/postMaterialIn',{barCode:this.code})
				}catch(e){
					this.$refs.message.error(e.message)
				}
				
				uni.$emit('startScan')
			},
			handleGoto(){
				Bus.$off("scancodedate",this.scanCodeCallback);
				uni.navigateTo({
					url:`/pages/materialIn/operator?barCode=${this.code}`
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
