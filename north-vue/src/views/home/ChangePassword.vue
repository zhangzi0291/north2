<style lang="less">

</style>

<template>
    <a-modal :onCancel="cancel" :onOk="ok" :title="'修改密码'" :visible="visible" :width="600">
    <a-form ref="form" :model="data" :rules="rules" v-bind="layout">
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
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, toRef} from 'vue'
import SysUserApi from "@/api/sys/SysUserApi";

export default defineComponent({
  name: 'ChangePassword',
  data() {
    return {
      visible: false,
      data: <any>{},
      userId: "",
      layout: {
        labelCol: {span: 8},
        wrapperCol: {span: 14},
      },
      rules: {
        oldPassword: [
          {required: true, type: 'string', trigger: 'blur', message: "旧密码不可为空"},
          {
            validator: (rule: any, value: any, callback: any) => {
              // @ts-ignore
              SysUserApi.checkPassword(this.userId, value).then(res => {
                if (res.data.code == 40001) {
                  callback(res.data.msg)
                } else if (res.data.code == 200) {
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
      SysUserApi.changePassword(this.userId, this.data.newPassword, this.data.oldPassword).then(() => {
        this.$message.success("操作成功")
        this.visible = false
      });
    },
    cancel() {
      this.visible = false
    },
    open(id: string) {
      this.visible = true
      this.userId = id
    },
  }

})

</script>

