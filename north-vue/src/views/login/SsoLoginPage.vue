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

        <a-form ref="loginForm" v-bind="layout" :model="loginData" :rules="loginRules" hideRequiredMark>
          <a-form-item label="用户名" name="username">
            <a-input v-model:value="loginData.username"/>
          </a-form-item>
          <a-form-item label="密码" name="password">
            <a-input-password v-model:value="loginData.password" autocomplete="off" type="password"/>
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
import {Options, Vue} from "vue-class-component";
import {AxiosResponse} from "axios";
import {NamePath} from "ant-design-vue/es/form/interface";
import SysLoginApi, {LoginData, RegisterData} from "@/api/SysLoginApi";
import BaseApi from "@/api/BaseApi";

const MD5 = require('md5.js')

@Options({
  name: 'Login',
  data() {
    return {
      url: {
        login: "/sysLogin/login"
      },
      isLogin: true,
      loginData: {},
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
    keydown(){
      if(this.isLogin){
        this.login()
      }else {
        this.register()
      }
    },
    login() {
      this.$refs.loginForm.validate().then((nameList: NamePath[]) => {
        this.tmpPwd = this.loginData.password;
        this.loginData.password = new MD5().update(this.loginData.password).digest('hex');
        let data: LoginData = this.loginData
        data.redirect = this.$route.query.redirect
        SysLoginApi.ssoLogin(data).then((res: AxiosResponse) => {
          console.log(res)
          this.loginData.password = this.tmpPwd;

          let href = res.data.data

          window.location.href = href
        }).catch(() => {
          this.loginData.password = this.tmpPwd;
        });
      })
    },
  },
  created() {
  },
  mounted() {
  },
  setup() {
  }
})

export default class Login extends Vue {
}

</script>
