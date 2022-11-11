<template>
		<my-page nav-title="原材料上架">
			
			<view class="main" slot="page-main">
				<view class="header m-b-8">
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
					<view v-if="materialInfo.recommendBinCode">
						<view class="text-line m-b-8 ">
							<view class="label">
								托盘类型：
							</view>
							{{materialInfo.palletType}}
						</view>
						<view class="text-line m-b-8 ">
							<view class="label">
								托盘编码：
							</view>
							{{materialInfo.palletCode}}
						</view>
						<view class="text-line m-b-8 ">
							<view class="label">
								推荐库位：
							</view>
							{{materialInfo.recommendBinCode}}
						</view>
					</view>
				</view>
			
				<view class="content">
					<uni-forms v-if="!materialInfo.recommendBinCode" :label-width="80" ref="palletForm" :rules="palletRules" :modelValue="palletForm" label-position="left">
						<uni-forms-item label="托盘类型" name="pallet" required>
							<uni-data-picker 
							placeholder="请选择托盘类型" 
							popup-title="请选择托盘类型" 
							:localdata="palletTypeList" 
							@change="handlePalletTypechange"
							v-model="palletForm.palletType">
							</uni-data-picker>
						</uni-forms-item>
						<uni-forms-item label="托盘编码" name="palletTypeCode" required>
							<uni-easyinput  v-model="palletForm.palletTypeCode"  placeholder="托盘编码" />
						</uni-forms-item>
						<uni-forms-item label="推荐库位" name="recommendBinCode" required>
							<uni-easyinput  v-model="palletForm.recommendBinCode" disabled placeholder="系统自动生成" />
						</uni-forms-item>
						<o-btn block class="submit-btn primary-button" :loading="submitLoading"  @click="handleGenerateInTask">提交</o-btn>
					</uni-forms>
					
					<!-- v-if="materialInfo.recommendBinCode" -->
					<uni-forms  v-if="materialInfo.recommendBinCode" :label-width="80" ref="binInForm" :rules="binInFormRules" :modelValue="binInForm" label-position="left">
						<uni-forms-item label="barCode" name="barCode" style="display: none;">
							<uni-easyinput  v-model="binInForm.barCode" />
						</uni-forms-item>
						<uni-forms-item label="SSCC码" name="mesBarCode" required>
							<view class="custom-input" :class="focusInput==='mesBarCode'?'focus':''" @click="()=>handleSetFocusInput('mesBarCode')">
								<text :class="!binInForm.mesBarCode?'placeholder-text':''">{{binInForm.mesBarCode||'请扫描SSCC码'}}</text>
							</view>
						</uni-forms-item>
						<uni-forms-item label="目标库位" name="recommendBinCode" required>
							<view class="custom-input" :class="focusInput==='recommendBinCode'?'focus':''" @click="()=>handleSetFocusInput('recommendBinCode')">
								<text :class="!binInForm.recommendBinCode?'placeholder-text':''">{{binInForm.recommendBinCode||'请扫描SSCC码'}}</text>
							</view>
						</uni-forms-item>
						<o-btn block class="submit-btn primary-button" :loading="submitLoading"  @click="handlePostBinIn">提交</o-btn>
					</uni-forms>
					
				</view>
			
				
				<uni-popup ref="alertDialog" type="dialog">
						<uni-popup-dialog 
							type="info" 
							cancelText="关闭" 
							confirmText="确认" 
							title="通知" 
							content="扫描库位与推荐库位不一致，是否提交!" 
							@confirm="onSubmitBinIn">
						</uni-popup-dialog>
				</uni-popup>
						
				<uni-popup ref="popup" :is-mask-click="false">
					<view class="result-content">
						<view class="result-status">
							<uni-icons 
							custom-prefix="iconfont"
							class="success-color"
							type="icon-chenggong" 
							size="32"></uni-icons>
							<text class=" text success-color">上架成功</text>
						</view>
						<view class="data-box" >
							<view class="text-line m-b-8">
								<view class="label">上架库位：</view>
								{{binInForm.recommendBinCode}} 
							</view>
							
						</view>
						<o-btn block class="primary-button" @click="handleGoBack">返回</o-btn>
					</view>
				</uni-popup>
			</view>
			<Message ref="message"></Message>
		</my-page>
	</view>
</template>

<script>
	import Message from '@/components/Message'
	import Bus from '@/utils/bus'
	
	function convertPalletList(rows){
		const list = rows.map(item =>{
			const text = `类型：${item.type}；宽：${item.width}；高：${item.height}`
			return {
				text,
				value:item.type
			}
		})
		return list
	}
	
	export default {
		components:{
			Message,
		},
		onLoad(options){
			this.barCode = options.barCode
			this.binInForm.barCode = options.barCode
			this.getByMesBarCode(options.barCode)
		},
		onLaunch() {
			Bus.$off("scancodedate");
		},
		data() {
			return {
				submitLoading:false,
				materialInfo:{},
				barCode:undefined,
				palletTypeList:[],
				palletForm:{
					
				},
				palletRules:{
					palletType:{
						rules: [{
							required: true,
							errorMessage: '请选择托盘类型',
						}]
					},
					palletTypeCode:{
						rules: [{
							required: true,
							errorMessage: '托盘编码不能为空',
						}]
					},
					recommendBinCode:{
						rules: [{
							required: true,
							errorMessage: '推荐库位不能为空',
						}]
					},
				},
				binInFormRules:{
					recommendBinCode:{
						rules: [{
							required: true,
							errorMessage: '推荐库位不能为空',
						}]
					},
					mesBarCode:{
						rules: [
							{
								required: true,
								errorMessage: 'SSCC码不能为空',
							},
							{
								validateFunction:function(rule,value,data,callback){
									if (value != data.barCode) {
										callback('当前SSCC码和扫描SSCC码不一致')
									}
									return true
								}
							},
						]
					}
				},
				palletForm:{
					palletTypeCode:undefined,
					palletType:undefined,
					recommendBinCode:undefined
				},
				binInForm:{
					barCode:undefined, //url barCode
					mesBarCode:undefined,
					recommendBinCode:undefined
				},
				
				focusInput:'mesBarCode'
			};
		},
		methods:{
			handleSetFocusInput(focusName){
				this.focusInput = focusName
				uni.hideKeyboard()
			},
			async initScanCode(){
				const _this = this
				Bus.$on('scancodedate',function(data){
					_this.binInForm[_this.focusInput] = data.code.trim()
				})
			},
			async handleGoBack(){
				uni.navigateBack({delta:1})	
			},
			async handlePalletTypechange(e) {
				const palletTypeId = e.detail.value[0].value
				await this.getPalletTypeCode(palletTypeId)
			},
			async getPalletList(){
				const data = await this.$store.dispatch('binIn/getPalletList')
				this.palletTypeList = convertPalletList(data)
			},
			async lodaData(){
				this.getPalletList()
			},
			async getPalletTypeCode(palletTypeId){
				const data = await this.$store.dispatch('binIn/getPalletTypeCode',palletTypeId)
				this.palletForm.palletTypeCode = data.virtualPalletCode
				await this.allocate()
			},
			async getByMesBarCode(barCode){
				try{
					const data = await this.$store.dispatch('binIn/getByMesBarCode',barCode)
					if(data && data.status ===1 ){
						throw Error('已上架，请勿重复操作')
					}
					this.materialInfo = data
					if(data.recommendBinCode){
						this.initScanCode()
					}
				}catch(e){
					this.$refs.message.error(e.message)
				}
			},
			async handleGenerateInTask(){
				this.$refs.palletForm
				  .validate()
				  .then(async (res) => {
					try{
						uni.showLoading({
							title:'正在提交'
						})
						this.submitLoading = true
						const options = {
							mesBarCode:this.barCode,
							palletCode:this.palletForm.palletTypeCode,
							palletType:this.palletForm.palletType,
							recommendBinCode:this.palletForm.recommendBinCode
						}
						await this.$store.dispatch('binIn/generateInTask',options)
						
						await this.getByMesBarCode(this.barCode)
					}catch(e){
						this.$refs.message.error(e.message)
					}finally{
						uni.hideLoading()
						this.submitLoading = false
					}
				  }).catch((err) => {});
			},
			async allocate(){
				try{
					const options = {
						mesBarCode:this.barCode,
						palletCode:this.palletForm.palletTypeCode,
						palletType:this.palletForm.palletType
					}
					const data = await this.$store.dispatch('binIn/allocate',options)
					this.palletForm.recommendBinCode = data.recommendBinCode
				}catch(e){
					this.$refs.message.error('获取推荐库位失败')
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
		},
		watch:{
			'binInForm.mesBarCode'(value){
				this.focusInput = 'recommendBinCode'
			}
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

.text-line{
		color: #333;
		font-size: 14px;
		display: flex;
		align-items: center;
		.label{
			width: 72px;
		}
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
	
</style>
