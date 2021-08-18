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
    <a-layout-content class="content">
      <a-card :title="'修改密码'" style="width: 450px">
        <a-form ref="form" v-bind="layout" :model="data" :rules="rules">
          <a-form-item label="用户名" name="username">
            <a-input v-model:value="data.username"/>
          </a-form-item>
          <a-form-item label="旧密码" name="oldPassword">
            <a-input-password v-model:value="data.oldPassword"/>
          </a-form-item>
          <a-form-item label="新密码" name="newPassword">
            <a-input-password v-model:value="data.newPassword"/>
          </a-form-item>
          <a-form-item label="重复密码" name="rePassword">
            <a-input-password v-model:value="data.rePassword"/>
          </a-form-item>
        </a-form>
        <a-form-item :wrapper-col="{ span: 24 }">
          <div class="button-group">
            <a-button type="primary" @click="ok">提交</a-button>
          </div>
        </a-form-item>
      </a-card>
    </a-layout-content>
    <a-layout-footer class="footer">
    </a-layout-footer>
  </a-layout>

</template>
<script lang="ts">
import {Options, Vue} from "vue-class-component";
import {AxiosResponse} from "axios";
import SysLoginApi from "@/api/SysLoginApi";
import {NamePath} from "ant-design-vue/es/form/interface";
import {toRef} from "vue";
import SysUserApi from "@/api/SysUserApi";

const MD5 = require('md5.js')
let api = new SysUserApi();

@Options({
  name: 'register',
  data() {
    return {
      data: {},
      title: window.title,
      layout: {
        labelCol: {span: 8},
        wrapperCol: {span: 14},
      },
      rules: {
        username: [
          {required: true, type: 'string', trigger: 'blur', message: "用户名不可为空"},
        ],
        oldPassword: [
          {required: true, type: 'string', trigger: 'blur', message: "旧密码不可为空"},
          {
            validator: (rule: any, value: any, callback: any) => {
              let data:any = this.data
              if(!data.username){
                return callback("用户名未填写")
              }
              api.checkPassword(this.userId, value,data.username).then(res => {
                if (res.data.code == '40001') {
                  callback(res.data.msg)
                } else if (res.data.code == '200') {
                  callback()
                } else {
                  callback("错误")
                }
              })
            }, trigger: 'blur',
          },
        ],
        newPassword: [
          {required: true, type: 'string', trigger: 'blur', message: "新密码不可为空"},
        ],
        rePassword: [
          {required: true, type: 'string', trigger: 'blur', message: "重复密码不可为空"},
          {
            validator: (rule: any, value: any, callback: any) => {
              let data: any = this.data
              if (toRef(data, "newPassword").value == value) {
                callback()
              } else {
                callback("两次密码不同")
              }
            }, trigger: 'blur',
          },
        ],
      }
    }
  },
  methods: {
    ok() {
      this.$refs.form.validate().then((nameList: NamePath[]) => {
        SysLoginApi.pyChangePassword(this.data.key, this.data.username, this.data.newPassword, this.data.oldPassword).then((res: AxiosResponse) => {
          this.$message.success("修改成功")
          this.data={}
        })
      })
    }
  },
  created() {
  },
  mounted() {
    this.data.key = this.$route.query.key
  },
  setup() {
  }
})

export default class Login extends Vue {
}

</script>
