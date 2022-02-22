<style lang="less">
</style>
<template>
  <div>
    <a-form>
      <a-form-item label="博客标题">
        <a-input v-model:value="data.weblogTitle" allowClear/>
      </a-form-item>
      <a-form-item label="标题图片">
        <upload-image @imageChange="imageChange" :init-file-list="fileList"></upload-image>
      </a-form-item>
      <a-form-item label="文章文本">
        <md-editor v-model="data.weblogText"/>
      </a-form-item>
    </a-form>

    <a-page-header>
      <template #extra>
        <a-button type="primary" @click="add()">发布</a-button>
      </template>
    </a-page-header>
  </div>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import WeblogApi from "@/api/WeblogApi";
import {AxiosResponse} from "axios";


export default defineComponent({
  name: 'UpdateWeblog',
  data() {
    return {
      data: {},
      fileList: [],
    }
  },
  methods: {
    get() {
      if (this.$route.params.id) {
        WeblogApi.get(this.$route.params.id).then(res => {
          this.data = res.data.data
          this.fileList = [{url: window.BASE_URL + '/sysFile/download?id=' + res.data.data.weblogTitleImage}]
        })
      }
    },
    add() {
      const formData = new FormData();
      formData.append('type', "1")
      formData.append('status', "0")
      for (let dataKey in this.data) {
        if (this.data[dataKey] != null) {
          formData.append(dataKey, this.data[dataKey])
        }
      }
      if (this.fileList.length > 0) {
        this.fileList.forEach((file: any) => {
          formData.append("files", file.originFileObj as any);
        });
      }
      console.log(this.fileList)
      console.log(formData)
      WeblogApi.add(formData).then((res: AxiosResponse) => {
        this.$message.success("操作成功")
      });
    },
    imageChange(fileList) {
      this.fileList = fileList
    }
  },
  mounted() {
    this.get()
  },
  setup() {

  }
})

</script>