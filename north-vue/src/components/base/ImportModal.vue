<style lang="less">

</style>
<template>
  <a-modal :onCancel="cancel" :onOk="ok" :title="title" :visible="visible">
    <a-form ref="form" v-bind="layout" :model="data" >
      <a-form-item >
        <a-upload :before-upload="beforeUpload" :file-list="fileList" @change="handleChange" :customRequest="customRequest">
          <a-button >
            <upload-outlined></upload-outlined>
            上传
          </a-button>
        </a-upload>
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script lang="ts">

import {Options, Vue} from 'vue-class-component';
import {AxiosResponse} from 'axios';
import {ModalField} from "@/components/base/FormModal.vue";

@Options({
  name: 'ImportModal',
  data() {
    return {
      visible: false,
      data: {},
      fileList: [],
      file:[],
      layout: {
        labelCol: {span: 8},
        wrapperCol: {span: 14},
      },
    }
  },
  props: {
    title: String,
    url: String,
    okCallback: {
      type: Function,
      default: (res: AxiosResponse) => {
        console.log(res)
      }
    },
    cancelCallback: {
      type: Function,
      default: () => {

      }
    }
  },
  methods: {
    ok() {
      let data = {}
      const formData = new FormData();
      formData.append("file", this.fileList[0].originFileObj)

      data = formData
      this.$refs.form.validate().then(() => {
        this.$axios({
          method: "post",
          url: this.url,
          data: data,
          responseType: 'blob',
        }).then((res: AxiosResponse) => {
          this.$message.success("操作成功")
          this.okCallback(res)
          this.visible = false
        });
      })

    },
    cancel() {
      this.cancelCallback()
      this.visible = false
    },
    open() {
      this.visible = true
    },
    beforeUpload(file: any) {
      if (!file) {
        return false
      }
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error("文件大小超出限制，最大2M");
        return false
      }

      return true
    },
    customRequest(file: any) {
      //不做任何实现不会自动上传
    },
    handleChange(fileItem: any) {
      let file = fileItem.file
      if (file.status == 'uploading') {
        file.status = 'done'
        this.fileList[0] = file
      }
      if (file.status == 'removed') {
        const index = this.fileList.indexOf(file);
        this.fileList.splice(index, 1);
      }
    },
  },
  created() {

  },

})


export default class FormModal extends Vue {
}

</script>