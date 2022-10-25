<style lang="less">

</style>

<template>
  <a-modal :onCancel="cancel" :onOk="ok" :title="'个人信息'" :visible="visible" :width="600">
    <a-form ref="form" :model="data" :rules="rules" v-bind="layout">
      <a-form-item label="昵称" name="nickname">
        <a-input v-model:value="data.nickname"/>
      </a-form-item>
      <a-form-item label="手机号" name="phone">
        <a-input v-model:value="data.phone"/>
      </a-form-item>
      <a-form-item label="E-mail" name="email">
        <a-input v-model:value="data.email"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent} from 'vue'
import {AxiosResponse} from "axios";
import SysUserApi from "@/api/sys/SysUserApi";

export default defineComponent({
  name: 'ChangePassword',
  data() {
    return {
      visible: false,
      data: {},
      userId: "",
      layout: {
        labelCol: {span: 8},
        wrapperCol: {span: 14},
      },
      rules: {
        nickname: [
          {required: true, type: 'string', trigger: 'blur', message: "旧密码不可为空"},
        ],
        phone: [
          {required: true, type: 'string', trigger: 'blur', message: "旧密码不可为空"},
        ],
        email: [
          {required: true, type: 'string', trigger: 'blur', message: "旧密码不可为空"},
        ],
      }
    }
  },
  methods: {
    ok() {
      // api.changePassword(this.userId, this.data.newPassword,this.data.oldPassword).then((res: AxiosResponse) => {
      //   this.$message.success("操作成功")
      //   this.visible = false
      // });
    },
    cancel() {
      this.visible = false
    },
    open(id: string) {
      this.visible = true
      this.userId = id
      SysUserApi.get(id).then((res: AxiosResponse) => {
        this.data = {
          nickname: res.data.data.nickname,
          phone: res.data.data.phone,
          email: res.data.data.email,
        }
      })
    },
  }
})

</script>

