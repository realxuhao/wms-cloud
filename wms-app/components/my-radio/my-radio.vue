<template>
	<view class="radio-main" @click="onChange">
		<view class="radio m-r-8" :class="checked?'active':''" >
			<view class="radio-icon"></view>
		</view>
		<slot></slot>
	</view>
</template>

<script>
	export default {
		name:"my-radio",
		props:{
			checked:{
				type:[Boolean,String],
				default:false
			}
		},
		data() {
			return {
				
			};
		},
		 model: {
			prop: 'checked',
			event: 'change'
		  },
		  methods:{
			  onChange(){
				  this.$emit('change',!this.checked)
			  }
		  }
	}
</script>

<style lang="scss">
.radio-main{
	display: flex;
	align-items: center;
}
.radio{
	width: 16px;
	height: 16px;
	border-radius: 16px;
	background-color: #fff;
	border: 1px solid #dcdfe6;
	display: flex;
	justify-content: center;
	align-items: center;
	.radio-icon{
		width: 8px;
		height: 8px;
		border-radius: 10px;
		opacity: 0;
	}
	&.active{
		border-color: $primary-color;
		.radio-icon{
			background-color:$primary-color;
			opacity: 1;
		}
	}
}
</style>