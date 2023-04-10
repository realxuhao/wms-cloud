<template>
	<view class="content">
		<view class="avator-wrapper">
			<view class="avator">
				<image class="img" src="../../static/logo.png" mode="widthFix"></image>
				<h2>WMS 仓储管理</h2>
			</view>
		</view>
		<view class="form">
			<uni-forms ref="form" :modelValue="formData" :rules="rules">
				<uni-forms-item name="username" label="账号" required><uni-easyinput v-model="formData.username" placeholder="请输入账号" /></uni-forms-item>
				<uni-forms-item name="password" label="密码" required><uni-easyinput type="password" v-model="formData.password" placeholder="请输入密码" /></uni-forms-item>
			</uni-forms>
			<o-btn block :loading="loading" loading-text="正在登录" @click="submit" class="loginBtn">登录</o-btn>
		</view>

		<Message ref="message"></Message>
	</view>
</template>

<script>
import { setToken } from '@/utils/storage';

import Message from '@/components/Message';

export default {
	components: {
		Message
	},
	data() {
		return {
			formData: {
				username: 'admin',
				password: 'nutricianlp123'
			},
			rules: {
				username: {
					rules: [
						{
							required: true,
							errorMessage: '请输入账号'
						}
					]
				},
				password: {
					rules: [
						{
							required: true,
							errorMessage: '请输入密码'
						}
					]
				}
			},
			loading: false
		};
	},
	onLoad() {},
	methods: {
		submit() {
			this.$refs.form
				.validate()
				.then(res => {
					this.login(res);
				})
				.catch(err => {});
		},
		async login(e) {
			try {
				this.loading = true;
				const token = await this.$store.dispatch('Login', this.formData);
				setToken(token);

				await this.$store.dispatch('GetInfo');

				uni.redirectTo({
					url: '/pages/home/index'
				});
			} catch (error) {
				this.$refs.message.error(error.message || '登录错误！');
			} finally {
				this.loading = false;
			}
		}
	}
};
</script>

<style lang="scss">
.wrapper {
	width: 100vw;
	height: 100vh;
}

.avator-wrapper {
	height: 240px;
	width: 100vw;
	background: $primary-color;
	margin-bottom: 32px;
	display: flex;
	justify-content: center;
	align-items: center;
	box-shadow: 0 2px 10px 0 #481d88;
}

.avator {
	color: #fff;
	image {
		margin-bottom: 12px;
	}
}

.avator .img {
	width: 100%;
}

.form {
	padding: 0 60upx;
}

.loginBtn {
	width: 100%;
	height: 80upx;
	background: linear-gradient(to right, $primary-color, #8471a9);
	border-radius: 50upx;
	margin-top: 32px !important;
	display: flex;
	justify-content: center;
	align-items: center;
	color: #fff !important;
}
</style>
