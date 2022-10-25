<style lang="less">
.title {
  text-align: center;
  color: #e0e0e0;
  font-size: 32px;
}

.content {
  display: flex;
  justify-content: center;
  align-items: center;

  .button-group {
    display: flex;
    justify-content: space-around;
    align-items: center;
  }
}
</style>
<template>

  <a-layout style="height:100%" @keyup.enter="keydown">
    <a-layout-header class="title">鉴权中心</a-layout-header>
    <a-layout-content class="content">
      <a-card :title="'登录'" style="width: 450px">

        <a-form ref="loginForm" :model="loginData" :rules="loginRules" hideRequiredMark v-bind="layout">
          <a-form-item label="用户名" name="username">
            <a-input v-model:value="loginData.username"/>
          </a-form-item>
          <a-form-item label="密码" name="password">
            <a-input-password v-model:value="loginData.password" autocomplete="off" type="password"/>
          </a-form-item>
          <a-form-item label="验证码" name="genId">
            <gen-slider @valid="valid"></gen-slider>
          </a-form-item>
          <a-form-item :wrapper-col="{ span: 24 }">
            <div class="button-group">
              <a-button type="primary" @click="login">登录</a-button>
            </div>
          </a-form-item>
        </a-form>

      </a-card>


    </a-layout-content>
    <a-layout-footer class="footer">

    </a-layout-footer>
  </a-layout>

</template>
<script lang="ts">
import {AxiosResponse} from "axios";
import {defineComponent} from "vue";
import GenSlider from "@/components/GenSlider.vue";
import BaseApi from "@/api/BaseApi";
import SysLoginApi, {LoginData} from "@/api/sys/SysLoginApi";

const MD5 = require('md5.js')
export default defineComponent({
  name: 'ssoLogin',
  components: {
    GenSlider
  },
  data() {
    return {
      url: {
        login: "/sysLogin/login"
      },
      isLogin: true,
      loginData: <any>{},
      registerData: {},
      tmpPwd: "",
      title: window.title,
      layout: {
        labelCol: {span: 8},
        wrapperCol: {span: 14},
      },
      loginRules: {
        username: [{required: true, type: 'string', trigger: 'blur', message: "用户名不可为空"}],
        password: [{required: true, type: 'string', trigger: 'blur', message: "密码不可为空"}],
        genId: [{required: true, type: 'string', trigger: 'blur', message: "需要校验"}],
      },
    }
  },
  computed: {
    changeTitle() {
      if (this.isLogin) {
        return '注册'
      } else {
        return '登录'
      }
    }
  },
  methods: {
    keydown() {
      if (this.isLogin) {
        this.login()
      } else {
        // this.register()
      }
    },
    login() {
      const loginForm: any = this.$refs.loginForm

      loginForm.validate().then(() => {
        this.tmpPwd = this.loginData.password;
        this.loginData.password = new MD5().update(this.loginData.password).digest('hex');
        let data: LoginData = this.loginData
        data.redirect = <string>this.$route.query.redirect
        data.hash = <string>this.$route.query.hash
        console.log(this.$route.query)
        console.log(data)
        SysLoginApi.ssoLogin(data).then((res: AxiosResponse) => {
          this.loginData.password = this.tmpPwd;
          let user = {
            userId: res.data.data.user.id,
            username: res.data.data.user.username,
            nickname: res.data.data.user.nickname,
            iconUrl: res.data.data.user.iconUrl,
          }
          BaseApi.setUserByStorage(user)
          BaseApi.setTokenByStorage(res.data.data.token)
          BaseApi.setPermissionsByStorage(res.data.data.permissions)
          BaseApi.setRolesByStorage(res.data.data.roles)

          window.location.href = res.data.data.redirect
        }).catch(() => {
          this.loginData.password = this.tmpPwd;
        });
      })
    },
    valid(valid) {
      console.log(valid)
      if (valid) {
        this.isValid = true
        this.loginData.genId = valid
      } else {
        this.isValid = false
      }
    },
  },
  created() {
  },
  mounted() {
  },
  setup() {
  }
})

</script>
