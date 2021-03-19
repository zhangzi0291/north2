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

  <a-layout style="height:100%" @keyup.enter="login">
    <a-layout-header class="title">{{ title }}</a-layout-header>
    <a-layout-content class="content">
      <a-card :title="'登陆'" style="width: 450px">
        <a-form ref="loginForm" v-bind="layout" :model="loginData" :rules="rules" hideRequiredMark>
          <a-form-item label="用户名" name="username">
            <a-input v-model:value="loginData.username"/>
          </a-form-item>
          <a-form-item label="密码" name="password">
            <a-input-password v-model:value="loginData.password" autocomplete="off" type="password"/>
          </a-form-item>
          <a-form-item :wrapper-col="{ span: 24 }">
            <div class="button-group">
              <!--              <a-button type="link">注册</a-button>-->
              <a-button type="primary" @click="login">登陆</a-button>
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
import SysLoginApi from "@/api/SysLoginApi";

const MD5 = require('md5.js')

const loginApi = new SysLoginApi()

@Options({
  name: 'Login',
  data() {
    return {
      url: {
        login: "/sysLogin/login"
      },
      loginData: {},
      tmpPwd: "",
      title: window.title,
      layout: {
        labelCol: {span: 8},
        wrapperCol: {span: 14},
      },
      rules: {
        username: [{required: true, type: 'string', trigger: 'blur', message: "用户名不可为空"}],
        password: [{required: true, type: 'string', trigger: 'blur', message: "密码不可为空"}],
      },
    }
  },
  methods: {
    login() {
      this.$refs.loginForm.validate().then((nameList: NamePath[]) => {
        this.tmpPwd = this.loginData.password;

        this.loginData.password = new MD5().update(this.loginData.password).digest('hex');
        loginApi.login(this.loginData.username, this.loginData.password).then((res: AxiosResponse) => {
          this.$message.success("登陆成功")
          this.loginData.password = this.tmpPwd;
          let user = {
            username:res.data.data.user.username,
            nickname:res.data.data.user.nickname,
            iconUrl:res.data.data.user.iconUrl,
          }
          window.localStorage.setItem("user",JSON.stringify(user))
          window.localStorage.setItem("token",res.data.data.token)
          document.cookie="satoken="+res.data.data.token
          this.$router.push({path: '/',})
        }).catch(() => {
          this.loginData.password = this.tmpPwd;
        });
      })

    }
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
