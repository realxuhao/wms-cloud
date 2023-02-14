<template>
		<my-page nav-title="IQC抽样确认">
			
			<view class="main" slot="page-main">
				<view class="header m-b-8">
					<view class="text-line m-b-8 ">
						<view class="label">物料名称：</view>
						{{sampleInfo.materialName}}
					</view>
					<view class="text-line m-b-8 ">
						<view class="label">物料编码：</view>
						{{sampleInfo.materialNb}}
					</view>
					<view class="text-line m-b-8 ">
						<view class="label">SSCC码：</view>
						{{sampleInfo.ssccNb}}
					</view>
					<view class="text-line m-b-8 ">
						<view class="label">批次号：</view>
						{{sampleInfo.batchNb}}
					</view>
					<view class="text-line m-b-8 ">
						<view class="label">Cell：</view>
						{{sampleInfo.cell}}
					</view>
					<view class="text-line m-b-8 ">
						<view class="label">推荐抽样数量：</view>
						{{sampleInfo.recommendSampleQuantity}} {{sampleInfo.unit}}
					</view>
					
				</view>
			
				<view class="content">		
					<uni-forms  :label-width="80" ref="form" :rules="rules" :modelValue="form" label-position="left">
						<uni-forms-item  label="抽样数量" name="sampleQuantity" required >
							<uni-easyinput v-model="form.sampleQuantity"  placeholder="请输入抽样数量" ></uni-easyinput>
						</uni-forms-item>
						<o-btn block class="submit-btn primary-button" :loading="submitLoading"  @click="handlePost">提交</o-btn>
					</uni-forms>
					
				</view>
			<!-- 
				<uni-popup ref="popup" :is-mask-click="false">
					<uni-popup-dialog before-close type="info" cancelText="取消" confirmText="确认" title="确认"  @confirm="handleGotoBinInOperation"
					@close="handleGoBack">
					<view>
						拆托完成！
					</view>
					</uni-popup-dialog>
				</uni-popup> -->
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
					sampleQuantity:{
						rules: [{
							required: true,
							errorMessage: '不能为空',
						}]
					},
				},
				form:{
					sampleQuantity:'',
				},
				sampleInfo:{}
			};
		},
		onLoad(options){
			this.code = options.barCode
			this.getSample(options.barCode)
		},
	
		methods:{
			async getSample(barCode){
				try{
					uni.showLoading({
						title:'识别中'
					})
					const data = await this.$store.dispatch('IQC/getSample',barCode)
					this.sampleInfo = data
				}catch(e){
					this.$refs.message.error(e.message)
				}finally{
					uni.hideLoading()
				}
			},
			handleGoBack(){
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
						ssccNb:this.sampleInfo.ssccNb
					}
					const data = await this.$store.dispatch('IQC/sampleConfirm',options)
					this.$refs.message.success('提交成功')
					setTimeout(()=>{
						this.handleGoBack()
					},1000)
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
		width: 100px;
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
