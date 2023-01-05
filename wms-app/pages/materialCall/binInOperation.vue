<template>
		<my-page nav-title="拆托">
			
			<view class="main" slot="page-main">
				<view class="header m-b-8">
					<view class="text-line m-b-8 ">
						<view class="label">源SSCC码：</view>
						{{params.ssccNumber}}
					</view>
					<view class="text-line m-b-8 ">
						<view class="label">拆托数量：</view>
						{{params.quantity}}
					</view>
				</view>
			
				<view class="content">		
					<uni-forms  :label-width="80" ref="form" :rules="rules" :modelValue="form" label-position="left">
						<uni-forms-item  label="拆托SSCC码" name="newMesBarCode" required >
							<view class="flex flex-ai">
								<view class="custom-input" :class="editFieldName==='form.newMesBarCode'?'focus':''">
									<text :class="!form.newMesBarCode?'placeholder-text':''">{{form.newMesBarCode||'请扫描拆托SSCC码'}}</text>
								</view>
							</view>
						</uni-forms-item>
						<uni-forms-item label="实际拆托数量" name="splitQuantity" required>
							<view class="custom-input" >
								<text >{{form.splitQuantity}}</text>
							</view>
						</uni-forms-item>
						<o-btn block class="submit-btn primary-button" :loading="submitLoading"  @click="handlePost">提交</o-btn>
					</uni-forms>
					
				</view>
			
				<uni-popup ref="popup" :is-mask-click="false">
					<uni-popup-dialog before-close type="info" cancelText="返回" confirmText="上架" title="已提交"  @confirm="handleGotoBinInOperation"
					@close="handleGoBack">
					<view>
						拆托完成！
					</view>
					</uni-popup-dialog>
				</uni-popup>
			</view>
			<Message ref="message"></Message>
		</my-page>
	</view>
</template>

<script>
	import Message from '@/components/Message'
	import Bus from '@/utils/bus'
	import _ from 'lodash'

	export default {
		components:{
			Message,
		},
		data() {
			return {
				params:{},
				code:'',
				submitLoading:false,
				rules:{
					newMesBarCode:{
						rules: [{
							required: true,
							errorMessage: '不能为空',
						}]
					},
					splitQuantity:{
						rules: [
							{
								required: true,
								errorMessage: '不能为空',
							},
						]
					}
				},
				form:{
					newMesBarCode:'',
					splitQuantity:'',
				},
				
				editFieldName:"form.newMesBarCode"  
			};
		},
		onLoad(options){
			this.params = options
			
			this.initScanCode()
		},
		onLaunch() {
			Bus.$off("scancodedate",this.scanCodeCallback);
		},
		methods:{
			handleGotoBinInOperation(){
				Bus.$off("scancodedate",this.scanCodeCallback);
				uni.redirectTo({
					url:`/pages/binIn/operation?barCode=${this.code}`
				})
			},
			handleSetEditFieldName(editFieldName){
				this.editFieldName = editFieldName
			},
			async initScanCode(){
				Bus.$on('scancodedate',(data)=>{
					Bus.on('stopScan')
					const code = data.code.trim()
					this.code = code
					this.getMesBarCodeInfo(code)
				})
			},
			async getMesBarCodeInfo(code){
				try{
					uni.showLoading({
						title:'识别中'
					})
					const data = await this.$store.dispatch('kanban/parsedBarCode',code)
					this.form.splitQuantity = data.quantity
					this.form.newMesBarCode = data.ssccNb
				}catch(e){
					this.$refs.message.error(e.message)
				}finally{
					Bus.on('startScan')
					uni.hideLoading()
				}
			},
			async handleGoBack(){
				uni.navigateBack({delta:1})	
			},
			async handlePost(){
				this.$refs.form
				  .validate()
				  .then((res) => {
					
					this.onSubmit()
				  })
				  .catch((err) => {});
			},
			async onSubmit(){
				try{
					uni.showLoading({
						title:'正在提交'
					})
					this.submitLoading = true
					
					const options = {
						...this.form,
						sourceSsccNb:this.params.ssccNumber
					}
					const data = await this.$store.dispatch('kanban/splitPallet',options)
					this.$refs.popup.open()
				}catch(e){
					this.$refs.message.error(e.message)
				}finally{
					uni.hideLoading()
					this.submitLoading = false
				}
				
			}
		},
		mounted() {
		},
	}
</script>

<style lang="scss">
.main{
		height: 100%;
		padding: 8px;
		box-sizing: border-box;
		display: flex;
		flex-direction: column;
	}

.header{
	background: #fff;
	padding: 8px;
	border-radius: 4px;
	.label{
		width: 80px;
	}
}

.content{
	background: #fff;
	padding: 8px 8px 40px;
	border-radius: 4px;
}

/deep/.uni-data-tree{
	background: #fff;
}
	
.result-content{
	width: 324px;
	padding: 12px;
	box-sizing: border-box;
	background: #fff;
	border-radius: 4px;
	.result-status{
		color: $uni-color-success;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-bottom: 16px;
		.text{
			margin-left: 8px;
			font-size: 14px;
		}
	}
	.label{
		width: 100px;
	}
	.data-box{
		margin-bottom: 16px;
		padding: 0px 8px;
	}
}
.flex{
	.custom-input{
		flex: 1;
	}
}
</style>
