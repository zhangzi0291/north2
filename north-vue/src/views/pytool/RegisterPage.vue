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
<!--    <a-layout-header class="title">{{ title }}</a-layout-header>-->
    <a-layout-content class="content">
      <a-card :title="'注册'" style="width: 450px">
        <template #extra>
          <a-button type="link" @click="open">客户端下载</a-button>
        </template>

        <a-form ref="registerForm" v-bind="layout" :model="registerData" :rules="registerRules" hideRequiredMark>
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
      <a-modal :onCancel="cancel" :onOk="ok" :title="'说明'" :visible="visible" :width="720">
        <div v-html='appText'></div>
      </a-modal>
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
  name: 'register',
  data() {
    return {
      url: {
        login: "/sysLogin/login"
      },
      visible : false,
      appText: window.appText,
      isLogin: false,
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
    keydown(){
      if(this.isLogin){
      }else {
        this.register()
      }
    },
    register() {
      this.$refs.registerForm.validate().then((nameList: NamePath[]) => {
        let data: RegisterData = this.registerData
        SysLoginApi.pyToolRegister(data).then((res: AxiosResponse) => {
          this.isLogin = true
          this.$message.success("注册成功")
          this.registerData={}
        })
      })
    },
    open(){
      this.visible = true
    },
    cancel() {
      this.visible = false
    },
    ok() {
      this.visible = false
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
