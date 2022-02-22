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
    <a-layout-header class="title">{{ title }}</a-layout-header>
    <a-layout-content class="content">
      <a-card :title="'登录'" style="width: 450px">
        <!--        <template #extra>-->
        <!--          <a-button type="link" @click="changeBox">{{ changeTitle }}</a-button>-->
        <!--        </template>-->

        <a-form v-if="isLogin" ref="loginForm" v-bind="layout" :model="loginData" :rules="loginRules" hideRequiredMark>
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

        <a-form v-else ref="registerForm" v-bind="layout" :model="registerData" :rules="registerRules" hideRequiredMark>
          <a-form-item label="用户名" name="username">
            <a-input v-model:value="registerData.username"/>
          </a-form-item>
          <a-form-item label="密码" name="password">
            <a-input-password v-model:value="registerData.password" autocomplete="off" type="password"/>
          </a-form-item>
          <a-form-item label="昵称" name="nickname">
            <a-input v-model:value="registerData.nickname"/>
          </a-form-item>
          <a-form-item label="手机号" name="phone">
            <a-input v-model:value="registerData.phone"/>
          </a-form-item>
          <a-form-item label="邮箱" name="email">
            <a-input v-model:value="registerData.email"/>
          </a-form-item>
          <a-form-item :wrapper-col="{ span: 24 }">
            <div class="button-group">
              <a-button type="primary" @click="register">注册</a-button>
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
import {defineComponent} from 'vue'
import {AxiosResponse} from "axios";
import {NamePath} from "ant-design-vue/es/form/interface";
import SysLoginApi, {LoginData, RegisterData} from "@/api/SysLoginApi";
import BaseApi from "@/api/BaseApi";
import GenSlider from "@/components/GenSlider.vue";

const MD5 = require('md5.js')

export default defineComponent({
  name: 'Login',
  components: {
    GenSlider
  },
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
        genId: [{required: true, type: 'string', trigger: 'blur', message: "需要校验"}],
      },
      registerRules: {
        username: [{required: true, type: 'string', trigger: 'blur', message: "用户名不可为空"}],
        password: [{required: true, type: 'string', trigger: 'blur', message: "密码不可为空"}],
        nickname: [{required: true, type: 'string', trigger: 'blur', message: "昵称不可为空"}],
        phone: [
          {required: true, type: 'string', trigger: 'blur', message: "手机号不可为空"},
          {
            type: 'number', trigger: 'blur', message: "只能是数字", transform: (value: string) => {
              return Number(value)
            }
          },
        ],
        email: [
          {required: true, type: 'string', trigger: 'blur', message: "邮箱不可为空"},
          {type: 'email', trigger: 'blur', message: "邮箱不符合规则"},
        ],
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
        this.register()
      }
    },
    login() {
      const loginForm: any = this.$refs.loginForm
      loginForm.validate().then((nameList: NamePath[]) => {
        let data: LoginData = <LoginData>this.loginData
        this.tmpPwd = data.password;
        data.password = new MD5().update(data.password).digest('hex');

        SysLoginApi.login(data).then((res: AxiosResponse) => {
          this.$message.success("登录成功")
          data.password = this.tmpPwd;
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

          // document.cookie="satoken="+res.data.data.token
          this.$router.push({path: '/',})
        }).catch(() => {
          data.password = this.tmpPwd;
        });
      })
    },
    register() {
      const registerForm: any = this.$refs.registerForm
      registerForm.validate().then((nameList: NamePath[]) => {
        let data: RegisterData = <RegisterData>this.registerData
        SysLoginApi.register(data).then((res: AxiosResponse) => {
          this.isLogin = true
          this.$message.success("注册成功")
        })
      })
    },
    changeBox() {
      this.isLogin = !this.isLogin
    },
    valid(valid) {
      console.log(valid)
      if (valid) {
        this.isValid = true
        this.loginData.genId = valid
      } else {
        this.isValid = false
      }
    }
  },
  created() {
  },
  mounted() {
  },
  setup() {
  }
})

</script>
