<template>
  <div class="main">
    <div class="header">
      <div class="logo">
        <img src="@/assets/logo.png" alt="">
      </div>
      <h3>WMS</h3>
    </div>
    <a-form-model
      layout="vertical"
      id="formLogin"
      ref="form"
      class="user-layout-login"
      :model="form"
      :rules="rules">
      <a-alert
        v-if="isLoginError"
        type="error"
        showIcon
        style="margin-bottom: 24px;"
        :message="loginErrorInfo"
        closable
        :after-close="handleCloseLoginError"
      />
      <a-form-model-item prop="username" label="账户：">
        <a-input v-model="form.username" size="large" placeholder="账户: admin" >
          <a-icon slot="prefix" type="user" :style="{ color: 'rgba(0,0,0,.25)' }"/>
        </a-input>
      </a-form-model-item>
      <a-form-model-item prop="password" label="密码">
        <a-input-password v-model="form.password" size="large" placeholder="密码: admin123">
          <a-icon slot="prefix" type="lock" :style="{ color: 'rgba(0,0,0,.25)' }"/>
        </a-input-password>
      </a-form-model-item>
      <a-row :gutter="16" v-if="captchaEnabled">
        <a-col class="gutter-row" :span="16">
          <a-form-model-item prop="code">
            <a-input v-model="form.code" size="large" type="text" autocomplete="off" placeholder="验证码">
              <a-icon slot="prefix" type="security-scan" :style="{ color: 'rgba(0,0,0,.25)' }"/>
            </a-input>
          </a-form-model-item>
        </a-col>
        <a-col class="gutter-row" :span="8">
          <img class="getCaptcha" :src="codeUrl" @click="getCode">
        </a-col>
      </a-row>
      <a-form-model-item prop="rememberMe">
        <a-checkbox :checked="form.rememberMe" @change="rememberMe">记住密码</a-checkbox>
      </a-form-model-item>
      <a-form-item style="margin-top:24px">
        <a-button
          size="large"
          type="primary"
          htmlType="submit"
          class="login-button"
          :loading="logining"
          :disabled="logining"
          @click="handleSubmit"
        >确定</a-button>
      </a-form-item>
    </a-form-model>
  </div>
</template>
<script>
import { mapActions } from 'vuex'
import { timeFix } from '@/utils/util'
import { setToken } from '@/utils/cookie'

import { LOGIN_USERNAME, LOGIN_PASSWORD, LOGIN_REMEMBERME } from '@/store/mutation-types'
import storage from 'store'

export default {
  components: {
  },
  data () {
    return {
      codeUrl: '',
      isLoginError: false,
      loginErrorInfo: '',
      form: {
        username: 'admin',
        password: 'admin123',
        code: undefined,
        uuid: '',
        rememberMe: false
      },
      rules: {
        username: [{ required: true, message: '请输入帐户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        code: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
      },
      logining: false,
      captchaEnabled: true
    }
  },
  created () {
    this.getStorage()
    this.getCode()
  },
  methods: {
    async getCode () {
      const res = await this.$store.dispatch('GetCodeImg')
      this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
      if (this.captchaEnabled) {
        this.codeUrl = 'data:image/gif;base64,' + res.img
        this.form.uuid = res.uuid
      }
    },
    getStorage () {
      const username = storage.get(LOGIN_USERNAME)
      const password = storage.get(LOGIN_PASSWORD)
      const rememberMe = storage.get(LOGIN_REMEMBERME)
      if (username) {
        this.form = {
          username: username,
          password: password,
          rememberMe: rememberMe
        }
      }
    },
    rememberMe (e) {
      this.form.rememberMe = e.target.checked
    },
    ...mapActions(['Login', 'Logout']),
    handleSubmit () {
      this.logining = true
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.form.rememberMe) {
            storage.set(LOGIN_USERNAME, this.form.username)
            storage.set(LOGIN_PASSWORD, this.form.password)
            storage.set(LOGIN_REMEMBERME, this.form.rememberMe)
          } else {
            storage.remove(LOGIN_USERNAME)
            storage.remove(LOGIN_PASSWORD)
            storage.remove(LOGIN_REMEMBERME)
          }

          this.Login(this.form)
            .then((token) => this.loginSuccess(token))
            .catch(err => this.requestFailed(err))
            .finally(() => {
              this.logining = false
            })
        } else {
          setTimeout(() => {
            this.logining = false
          }, 600)
        }
      })
    },

    loginSuccess (token) {
      setToken(token)

      this.$router.push({ path: '/' })

      // 延迟 1 秒显示欢迎信息
      setTimeout(() => {
        this.$notification.success({
          message: '欢迎',
          description: `${timeFix()}，欢迎回来`
        })
      }, 1000)

      this.handleCloseLoginError()
    },

    requestFailed (err) {
      this.isLoginError = true
      this.loginErrorInfo = err
      this.form.code = undefined
      if (this.captchaEnabled) {
        this.getCode()
      }
    },
    handleCloseLoginError () {
      this.isLoginError = false
      this.loginErrorInfo = ''
    }
  }
}

</script>

<style lang="less" scoped>
.main{
  width: 380px;
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  right: 240px;
}

.header{
  display: flex;
  justify-content: center;
  align-items: center;
  color: #fff;
  margin-bottom: 16px;
  .logo{
    height: 64px;
    img{
      height: 100%;
    }
  }
  h3{
    font-size:32px;
    color: #fff;
    margin: 12px 0 0 8px;
  }
}

/deep/.ant-form-item-label > label{
  color: #fff;
}

.user-layout-login {
  label {
    font-size: 14px;
    color: #fff;
  }
  .getCaptcha {
    display: block;
    width: 100%;
    height: 40px;
  }
  button.login-button {
    padding: 0 15px;
    font-size: 16px;
    height: 40px;
    width: 100%;
  }
  .user-login-other {
    text-align: left;
    margin-top: 24px;
    line-height: 22px;
    .register {
      float: right;
    }
  }
}

</style>
