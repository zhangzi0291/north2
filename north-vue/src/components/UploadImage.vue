<style scoped>

</style>

<template>
  <div>
    <a-upload :before-upload="beforeUpload" :customRequest="customRequest" :file-list="fileList"
              accept="image/*" list-type="picture-card"
              @change="handleChange($event, item)" @preview="handlePreview"
    >
      <div v-if="fileList.length < maxImageNum">
        <plus-outlined/>
        <div class="ant-upload-text">上传</div>
      </div>

    </a-upload>
    <a-modal :footer="null" :visible="previewVisible" centered @cancel="previewCancel">
      <img :src="previewImage" style="width: 100%"/>
    </a-modal>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";

export default defineComponent({
  name: "UploadImage",
  data() {
    return {
      fileList: [],
      previewVisible: false,
      previewImage: "",
    }
  },
  props: {
    initFileList: {
      type: Array,
      default: () => {
        return []
      }
    },
    maxImageNum: {
      type: Number,
      default: () => {
        return 1
      }
    },
    maxImageSize: {
      type: Number,
      default: () => {
        return 5
      }
    }
  },
  methods: {
    beforeUpload(file: any) {
      if (!file) {
        return false
      }
      const isLt = file.size / 1024 / 1024 < this.maxImageSize;
      if (!isLt) {
        this.$message.error("文件大小超出限制，最大" + this.maxImageSize + "M");
        return false
      }

      return true
    },
    handleChange(fileItem: any) {
      let file = fileItem.file
      if (file.status == 'uploading') {
        file.status = 'done'
        this.fileList = [...this.fileList, file];
      }
      if (file.status == 'removed') {
        const index = this.fileList.indexOf(file);
        this.fileList.splice(index, 1);
      }
      this.$emit("imageChange", this.fileList)
    },
    customRequest(file: any) {
      //不做任何实现不会自动上传
    },
    async handlePreview(file: any) {
      if (file.url) {
        this.previewImage = file.url
      } else {
        this.previewImage = await this.loadImage(file.originFileObj)
      }
      this.previewVisible = true
    },
    loadImage(file: any) {
      return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result as string);
        reader.onerror = error => reject(error);
      });
    },
    previewCancel() {
      this.previewVisible = false
    },
  },
  watch: {
    'initFileList': function (newName, oldName) {
      this.fileList = newName
    },
  }
})
</script>

