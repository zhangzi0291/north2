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
import SysLoginApi, {LoginData} from "@/api/SysLoginApi";
import {defineComponent} from "vue";
import BaseApi from "@/api/BaseApi";

const MD5 = require('md5.js')
export default defineComponent({
  name: 'ssoLogin',
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
        // this.register()
      }
    },
    login() {
      const loginForm:any = this.$refs.loginForm

      loginForm.validate().then((nameList: NamePath[]) => {
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

          let href = res.data.data.redirect
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

</script>
