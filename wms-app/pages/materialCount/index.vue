<template>
		<my-page nav-title="原材料入库">
			<view class="main" slot="page-main">
				<view class="header m-b-8">
					<view class="text-line m-b-8 ">
						物料名称：{{materialInfo.materialName}}
					</view>
					<view class="text-line m-b-8 ">
						物料编码：{{materialInfo.materialNb}}
					</view>
					<view class="text-line m-b-8">
						总托数：<uni-tag size="small" type="success" :circle="false" :text="materialInfo.checkTypeDesc"></uni-tag>
					</view>
					<view class="text-line m-b-8">
						抽样件数：<uni-tag size="small" type="error" :circle="false" :text="materialInfo.checkQuantity+''"></uni-tag>
					</view>
					<view class="text-line ">
						抽样方式：<uni-tag size="small" type="success" :circle="false" :text="materialInfo.checkTypeDesc"></uni-tag>
					</view>
				</view>
				<view class="content">
						<uni-forms :label-width="80" v-if="materialInfo.checkType===1" ref="numberform" :rules="numberRules" :modelValue="numberFormData" label-position="left">
							<uni-forms-item label="实际件数" name="actualQuantity" required>
								<uni-easyinput type="number" v-model="numberFormData.actualQuantity" placeholder="实际抽样件数" />
							</uni-forms-item>
							<uni-forms-item label="数数结果" name="actualResult" required>
								<uni-easyinput v-model="numberFormData.actualResult" placeholder="数数结果" />
							</uni-forms-item>
							
							<o-btn block class="submit-btn" @click="handleNumberSubmit">提交</o-btn>
						</uni-forms>
					
					
						<uni-forms :label-width="80" v-if="materialInfo.checkType===0" ref="weightform" :rules="weightFormRules" :modelValue="weightFormData"  label-position="left">
							<view class="count-item m-b-8" v-for="(item,index) in weightList" :key="index">
								<view>
									<uni-forms-item 
									label="重量" 
									:name="'weight'+item" required 
									:rules="[{
									required: true,
									errorMessage: '请输入称重重量',
									}]"
								>
										<uni-easyinput v-model="weightFormData[`weight${item}`]" placeholder="请输入重量" />
									</uni-forms-item>
									<uni-forms-item 
									class="no-margin"
									label="件数" 
									:name="'number'+item" 
									required 
									:rules="[{
									required: true,
									errorMessage: '请输入称重件数',
									}]">
										<uni-easyinput v-model="weightFormData[`number${item}`]" placeholder="请输入件数" />
									</uni-forms-item>
								</view>
							
								<view class="action">
									<uni-tag @click="handleDeleteWeight(item)" v-show="weightList.length>1" type="warning" class="action-delte  m-b-8" text="删除" />
									<uni-tag type="success	" class="action-add" @click="handleAddWeight" text="新增" />
								</view>
							</view>
							<o-btn block class="submit-btn" @click="handleWeightSubmit">提交</o-btn>
							</uni-forms>
				</view>
			</view>
			
			<Message ref="message"></Message>
			
			
		</my-page>
</template>

<script>
	import Message from '@/components/Message'
	
	function convertWeightForm (weightForm){
		const keys =  Object.keys(weightForm)
		const weightList = keys.filter(item => item.indexOf('weight')>-1)
		const numberList = keys.filter(item => item.indexOf('weight')>-1)
		
		const actualResult = weightList.reduce((count,item)=>{
			const number = Number(weightForm[item])
			return count+number
		},0)
		const actualQuantity = weightList.reduce((count,item)=>{
			const number = Number(weightForm[item])
			return count+number
		},0)
		
		return {
			actualResult,
			actualQuantity
		}
	}
	
	export default {
	components:{
		Message	
	},
		name:"MaterialCount",
		onLoad: function (options) { 
			this.barCode = options.barCode
			this.getMaterialInfo(options.barCode)
		},
		data() {
			return {
				barCode:'',
				materialInfo:{
					checkQuantity:'',
					checkTypeDesc:''
				},
				
				numberFormData:{
					actualQuantity:undefined,
					actualResult:undefined,
				},
				numberRules:{
					actualQuantity:{
						rules: [{
							required: true,
							errorMessage: '请输入实际抽样件数',
						}]
					
					},
					actualResult:{
						rules: [{
							required: true,
							errorMessage: '请输入数数结果',
						}]
					
					},
				},
				weightFormData:{
					weight0:undefined,
					number0:undefined
				},
				weightFormRules:{
					actualQuantity:{
						rules: [{
							required: true,
							errorMessage: '请输入实际件数',
						}]
					}
				},
				weightList:[
					0
				],
			}
		},
		methods: {
			async getMaterialInfo(barCode){
				try{
					uni.showLoading()
					const data = await this.$store.dispatch('materialIn/getAndCheckMaterialIn',barCode)
					this.materialInfo = data
				}catch(e){
					this.$refs.message.error(e.message)
				}finally{
					uni.hideLoading()
				}
			},
			handleAddWeight(){
				const maxCount = Math.max.apply(null,this.weightList);
				this.weightList.push((Number(maxCount)+1))
			},
			handleDeleteWeight(index){
				this.weightList.splice(index,1)
				
				delete this.weightFormData[`weight${index}`]
				delete this.weightFormData[`number${index}`]
			},
			handleWeightSubmit(){
				this.$refs.weightform
				  .validate()
				  .then((res) => {
					  const data = convertWeightForm(res)
				    this.postMaterialIn(data);
				  })
				  .catch((err) => {});
			},
			handleNumberSubmit(){
				this.$refs.numberform
				  .validate()
				  .then((res) => {
				    this.postMaterialIn(res);
				  })
				  .catch((err) => {});
			},
			async postMaterialIn(data){
				try{
					uni.showLoading()
					await this.$store.dispatch('materialIn/postMaterialIn',{...data,mesBarCode:this.barCode})
					
					this.$refs.message.success('入库成功！')
					
					uni.navigateBack({delta:1})
				}catch(e){
					this.$refs.message.error(e.message)
				}finally{
					uni.hideLoading()
				}
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
	.text-line{
		color: #333;
		font-size: 14px;
	}
	/deep/.uni-tag{
		display: inline-block;
		min-width: 80px;
	}
}
.content{
	background: #fff;
	padding: 8px 8px 40px;
	border-radius: 4px;
	flex: 1;
	.count-item{
		background:rgb(241,242,247);
		border-radius: 4px;
		padding: 10px 4px;
		display: flex;
		/deep/.uni-easyinput{
			width: 200px;
		}
		.action{
			// flex: 1;
			width: 100px;
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;
			.action-remove{
				display: none;
			}
		}
	}
	.count-item:nth-last-child(1){
		.action-remove{
			display: block;
		}
	}
	.uni-tag{
		float: right;
	}
	.submit-btn{
		margin-top: 48px;
		background: $primary-color;
		color: #fff;
	}
}
/deep/.uni-forms{
	margin-bottom: 24px;
}

.no-margin{
	margin-bottom: 8px;
}

</style>
