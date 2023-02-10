<template>
	<my-page nav-title="扫描SSCC码" :shadow="false" :border="false">
			<view class="content" slot="page-main" >
				<image src="/static/sku-phone.png" class="m-b-8"></image>
				<text>请将激光扫描头对准SSCC码区域</text>
			</view>
			<Message ref="message"></Message>
			<uni-popup ref="submitLine" type="dialog">
				<uni-popup-dialog before-close type="success" cancelText="返回" confirmText="继续扫描" title="已提交"  @confirm="handleContinue"
				@close="handleGoBack">
				<view>
					请立即配送产线！
				</view>
				</uni-popup-dialog>
			</uni-popup>
	</my-page>
</template>

<script>
	import Message from '@/components/Message'
	import Bus from '@/utils/bus'
	
	const moveTypeMap = {
		0:'整托',
		1:'拆托'
	}
	
	export default {
		components:{
			Message,
		},
		onShow(){
			Bus.$on('scancodedate',this.scanCodeCallback)
		},
		destroyed() {
			Bus.$off("scancodedate",this.scanCodeCallback);
			console.log('destroyed')
		},
		data() {
			return {
				code:'',
				barCodeInfo:{}
			};
		},
		computed:{
			moveTypeMap:()=>moveTypeMap	
		},
		methods:{
			async scanCodeCallback(data){
				Bus.$emit('stopScan')
				this.code = data.code
				this.checkTask(data.code)
			},
			handleGoBack(){
				this.$refs.submitLine.close()
				uni.navigateBack({
					delta:1
				})
			},
			handleContinue(){
				Bus.$emit('startScan')
				this.$refs.submitLine.close()
			},
			async checkTask(barCode){
				try{
					uni.showLoading()
					const data = await this.$store.dispatch('kanban/checkKanbanTask',{mesBarCode:barCode})
					this.barCodeInfo = data
					// 拆托页面
					if(data.status === 1 && data.type === 1){
						this.handleGotoSplitPallet()
						return
					}else if(data.status !== 1){
						throw Error('此托不在捡配待下架清单中，请检查')
					}
					await this.$store.dispatch('materialCall/binDown',this.barCodeInfo.ssccNumber)
					this.$refs.submitLine.open()
				}catch(e){
					this.$refs.message.error(e.message)
				}finally{
					Bus.$emit('startScan')
					uni.hideLoading()
				}
			},
			handleGotoSplitPallet(){
				Bus.$off("scancodedate",this.scanCodeCallback);
				const {quantity,ssccNumber} = this.barCodeInfo
				uni.redirectTo({
					url:`/pages/splitPallet/splitPallet?ssccNumber=${ssccNumber}&quantity=${quantity}&mesBarCode=${this.code}`
				})
			},
			async submitDeliver(){
				try{
					uni.showLoading({
						title:'正在提交'
					})
					await this.$store.dispatch('kanban/deliver',{sscc:this.barCodeInfo.ssccNumber})
					this.$refs.submitLine.open()
				}catch(e){
					this.$refs.message.error(e.message)
				}finally{
					uni.hideLoading()
				}
			},
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
