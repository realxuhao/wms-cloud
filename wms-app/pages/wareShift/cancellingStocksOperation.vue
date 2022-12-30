<template>
		<my-page nav-title="新增产线退料">
			
			<view class="main" slot="page-main">
				<view class="header m-b-8">
					<view class="text-line m-b-8 ">
						<view class="label">批次：</view>
						{{materialInfo.ssccNumber}}
					</view>
					<view class="text-line m-b-8 ">
						<view class="label">有效期：</view>
						{{materialInfo.ssccNumber}}
					</view>
					<view class="text-line m-b-8 ">
						<view class="label">单位：</view>
						{{materialInfo.ssccNumber}}
					</view>
					<view class="text-line m-b-8 ">
						<view class="label">SSCC码：</view>
						{{materialInfo.ssccNumber}}
					</view>
					<view class="text-line m-b-8 ">
						<view class="label">物料名称：</view>
						{{materialInfo.materialName}}
					</view>
					<view class="text-line m-b-8 ">
						<view class="label">
							物料编码：
						</view>
						{{materialInfo.materialNb}}
					</view>
				</view>
			
				<view class="content">		
					<uni-forms  :label-width="80" ref="form" :rules="formRules" :modelValue="form" label-position="left">
						<uni-forms-item  label="仓库" name="palletTypeCode" required >
							<uni-easyinput type="number" v-model="form.number" placeholder="仓库" />
						</uni-forms-item>
						<uni-forms-item  label="数量" name="palletTypeCode" required >
							<uni-easyinput type="number" v-model="form.number" placeholder="数量" />
						</uni-forms-item>
						<uni-forms-item label="状态" name="status" required>
							<uni-data-checkbox v-model="form.status" :localdata="radioList"></uni-data-checkbox>
						</uni-forms-item>
						<o-btn block class="submit-btn primary-button" :loading="submitLoading"  @click="handlePostBinIn">提交</o-btn>
					</uni-forms>
					
				</view>
			</view>
			<Message ref="message"></Message>
		</my-page>
	</view>
</template>

<script>
	import Message from '@/components/Message'
	import Bus from '@/utils/bus'
	import _ from 'lodash'
	
	const radioList = [
		{
			text: '正常退料',
			value: 0
		},
		{
			text: '异常退料',
			value: 1
		},
	]
	export default {
		components:{
			Message,
		},
		data() {
			return {
				submitLoading:false,
				materialInfo:{},
				barCode:undefined,
				formRules:{
					recommendBinCode:{
						rules: [{
							required: true,
							errorMessage: '目标库位不能为空',
						}]
					},
					palletTypeCode:{
						rules: [
							{
								required: true,
								errorMessage: '托盘编码不能为空',
							},
						]
					}
				},
				form:{
					number:undefined,
					status:0
				},
				
			};
		},
		computed:{
			radioList(){
				return radioList
			}
		},
		onLoad(options){
			this.barCode = options.barCode
			this.binInForm.barCode = options.barCode
			this.getByMesBarCode(options.barCode)
		},
		methods:{
			async handleGoBack(){
				uni.navigateBack({delta:1})	
			},
		
			async lodaData(){
				// this.getPalletList()
			},
			async getByMesBarCode(barCode){
				try{
					const data = await this.$store.dispatch('wareShift/getByMesBarCode',barCode)
					if(data && data.status ===1 ){
						throw Error('已上架，请勿重复操作')
					}
					if(data.palletCode){
						this.editFieldName = 'binInForm.recommendBinCode'
					}
					this.materialInfo = data
				}catch(e){
					this.$refs.message.error(e.message)
				}
			},
			async handlePostBinIn(){
				this.$refs.binInForm
				  .validate()
				  .then((res) => {
					if(this.binInForm.recommendBinCode!==this.materialInfo.recommendBinCode){
						this.$refs.alertDialog.open()
						return
					}
					this.onSubmitBinIn()
				  })
				  .catch((err) => {});
			},
			async onSubmitBinIn(){
				try{
					uni.showLoading({
						title:'正在提交'
					})
					this.submitLoading = true
					
					const options = {
						mesBarCode:this.barCode,
						actualBinCode:this.binInForm.recommendBinCode,
						palletCode:this.binInForm.palletTypeCode
					}
					const data = await this.$store.dispatch('binIn/postBinIn',options)
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
			this.lodaData()
		}
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
