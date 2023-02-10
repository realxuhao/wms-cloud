<template>
	<my-page nav-title="拆托">
		<view class="main" slot="page-main">
			<view class="header m-b-8">
				<view class="text-line m-b-8 ">
					<view class="label">源SSCC：</view>


					<input class="custom-input" focus placeholder="扫描源SSCC" v-model="form.mesBarCode" @focus="handleSetEditFieldName('form.mesBarCode')"
						@blur="handleLeaveFocus" />

				</view>
				<view class="text-line m-b-8 ">
					<view class="label">物料编码：</view>
					{{stockInfo.materialNb}}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">物料名称：</view>
					{{stockInfo.materialName}}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">库位编码：</view>
					{{stockInfo.binCode}}
				</view>
				<view class="text-line m-b-8 ">
					<view class="label">最大拆托数量：</view>
					{{stockInfo.availableStock}}
				</view>
			</view>

			<view class="content">
				<uni-forms :label-width="110" ref="form" :rules="rules" :modelValue="form" label-position="left">
					<uni-forms-item label="拆托SSCC码" name="ssccNumber" required>
						<input class="custom-input" focus placeholder="扫描拆托mesBarCode" v-model="form.newMesBarCode" @focus="handleSetEditFieldName('form.newMesBarCode')"
							@blur="handleLeaveFocus" />
						<!-- <view class="custom-input">
							<text
								:class="!form.ssccNumber?'placeholder-text':''">{{form.ssccNumber||'请扫描拆托SSCC码'}}</text>
						</view> -->
					</uni-forms-item>
					<uni-forms-item label="实际拆托数量" name="splitQuantity" required>
						<view class="custom-input">
							<text>{{form.splitQuantity}}</text>
						</view>
					</uni-forms-item>
					<o-btn block class="submit-btn primary-button" :loading="submitLoading" @click="handlePost">提交
					</o-btn>
				</uni-forms>

			</view>

			<uni-popup ref="popup" :is-mask-click="false">
				<uni-popup-dialog before-close type="info" cancelText="返回" confirmText="原托上架" title="拆托完成!"
					@confirm="handleGotoBinInOperation" @close="handleGoBack">
					<view>
						请将拆托配送产线。
					</view>
				</uni-popup-dialog>
			</uni-popup>
		</view>
		<Message ref="message"></Message>
	</my-page>
</template>

<script>
	import Message from '@/components/Message'
	import Bus from '@/utils/bus'
	import _ from 'lodash'

	export default {
		name: "addSplit",
		components: {
			Message,
		},
		data() {
			return {
				params: {},
				code: '',
				submitLoading: false,
				rules: {
					ssccNumber: {
						rules: [{
							required: true,
							errorMessage: '不能为空',
						}]
					},
					splitQuantity: {
						rules: [{
							required: true,
							errorMessage: '不能为空',
						}, ]
					}
				},
				stockInfo: {},
				form: {
					mesBarCode: '',
					newMesBarCode: '',
					splitQuantity: '',
				},
				parsedMesBarCode: {},

				editFieldName: 'form.newMesBarCode'
				
			};
		},
		onLoad(options) {
			this.params = options
			this.initScanCode()
			this.form.mesBarCode = ''
		},
		
		onLaunch() {
			Bus.$off("scancodedate");
		},
		methods: {
			async handleLeaveFocus(e) {
				
				const inputText = e.target.value
				if (this.editFieldName === 'form.mesBarCode'){
					if (inputText.length === 50) {
						const data = await this.$store.dispatch('stock/getInfoByMesBarCode', inputText)
						this.stockInfo = data
					}else if (inputText.length === 18){
						const data = await this.$store.dispatch('stock/getInfoBySscc', inputText)
						this.stockInfo = data
					}
				}else if (this.editFieldName === 'form.newMesBarCode'){
					if (inputText.length === 50) {
						this.getMesBarCodeInfo(inputText)
						this.form.splitQuantity = this.parsedMesBarCode.quantity
					}
				}

			},
			async handleSetEditFieldName(editFieldName) {
				this.editFieldName = editFieldName
			
			},
			async initScanCode(){
				Bus.$on('scancodedate',(data)=>{
					
					const code = data.code.trim()
					// this.code = code
					// this.getMesBarCodeInfo(code)
				
					if(this.editFieldName==='form.mesBarCode'){
						this.getMesBarCodeInfo(code)
						
						console.log(this.parsedMesBarCode.ssccNb)
						this.form.mesBarCode = this.parsedMesBarCode.ssccNb
						console.log(this.form.mesBarCode)
						this.getStockInfo(this.parsedMesBarCode.ssccNb)
						// _.set(this,this.editFieldName,code)
					} else if(this.editFieldName === 'form.newMesBarCode'){
						this.getMesBarCodeInfo(code)
						this.form.newMesBarCode = this.parsedMesBarCode.ssccNb
						this.form.splitQuantity = this.parsedMesBarCode.quantity
					}
				})
			},
			async getStockInfo(code) {
				if (code.length === 50) {
					const data = await this.$store.dispatch('stock/getInfoByMesBarCode', code)
					this.stockInfo = data
				}else {
					const data = await this.$store.dispatch('stock/getInfoBySscc', code)
					this.stockInfo = data
				}
			},
			async getMesBarCodeInfo(code) {
				try {
					uni.showLoading({
						title: '识别中'
					})
					const data = await this.$store.dispatch('kanban/parsedBarCode', code)
					this.parsedMesBarCode = data
				} catch (e) {
					this.$refs.message.error(e.message)
				} finally {
					uni.hideLoading()
				}
			},
			
			
			async handleGoBack() {
				uni.navigateBack({
					delta: 1
				})
			},
			async handlePost() {
				this.$refs.form
					.validate()
					.then((res) => {

						this.onSubmit()
					})
					.catch((err) => {});
			},
			handleGotoBinInOperation() {
				uni.redirectTo({
					url: `/pages/binIn/operation?barCode=${this.params.mesBarCode}`
				})
			},
			async onSubmit() {
				try {
					uni.showLoading({
						title: '正在提交'
					})
					this.submitLoading = true

					const options = {
						...this.form,
						sourceSsccNb: this.params.ssccNumber
					}
					const data = await this.$store.dispatch('kanban/splitPallet', options)
					this.$refs.popup.open()
				} catch (e) {
					this.$refs.message.error(e.message)
				} finally {
					uni.hideLoading()
					this.submitLoading = false
				}
			}
		}
	}
</script>

<style lang="scss">
	.main {
		height: 100%;
		padding: 8px;
		box-sizing: border-box;
		display: flex;
		flex-direction: column;
	}

	.header {
		background: #fff;
		padding: 8px;
		border-radius: 4px;

		.label {
			width: 80px;
		}
	}

	.content {
		background: #fff;
		padding: 8px 8px 40px;
		border-radius: 4px;
	}

	/deep/.uni-data-tree {
		background: #fff;
	}

	.text-line {
		.label {
			width: 100px;
		}
	}

	.result-content {
		width: 324px;
		padding: 12px;
		box-sizing: border-box;
		background: #fff;
		border-radius: 4px;

		.result-status {
			color: $uni-color-success;
			display: flex;
			align-items: center;
			justify-content: center;
			margin-bottom: 16px;

			.text {
				margin-left: 8px;
				font-size: 14px;
			}
		}

		.label {
			width: 100px;
		}

		.data-box {
			margin-bottom: 16px;
			padding: 0px 8px;
		}
	}

	.flex {
		.custom-input {
			flex: 1;
		}
	}
</style>
