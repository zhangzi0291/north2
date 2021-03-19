<style lang="less">

</style>
<template>
  <a-modal :onCancel="cancel" :onOk="ok" :title="title" :visible="visible">
    <a-form ref="form" v-bind="layout" :model="data" :rules="rules">
      <template v-for="item in columns" :key="item.key+item.title">
        <!--  插槽  -->
        <a-form-item v-if="canEdit(item.key) && item.type == 'Slot'" :label="item.title" :name="item.key">
          <slot :data="data" :name="item.slot"/>
        </a-form-item>
        <!--  字符串  -->
        <a-form-item v-else-if="canEdit(item.key) && item.type == 'String'" :label="item.title" :name="item.key">
          <a-input v-model:value="data[item.key]" allowClear/>
        </a-form-item>
        <!--  文本框  -->
        <a-form-item v-else-if="canEdit(item.key) && item.type == 'Textarea'" :label="item.title" :name="item.key">
          <a-textarea v-model:value="data[item.key]" allowClear/>
        </a-form-item>
        <!--  密码  -->
        <a-form-item v-else-if="canEdit(item.key) && item.type == 'Password'" :label="item.title" :name="item.key">
          <a-input-password v-model:value="data[item.key]"/>
        </a-form-item>
        <!--  数字  -->
        <a-form-item v-else-if="canEdit(item.key) && item.type == 'Number'" :label="item.title" :name="item.key">
          <a-input-number v-model:value="data[item.key]">
            {{ data[item.key] }}
          </a-input-number>
        </a-form-item>
        <!--  下拉框  -->
        <a-form-item v-else-if="canEdit(item.key) && item.type == 'Select'" :label="item.title" :name="item.key">
          <a-select v-model:value="data[item.key]">
            <template v-for="select in item.array">
              <a-select-option :value="select.value">
                {{ select.lable }}
              </a-select-option>
            </template>
          </a-select>
        </a-form-item>
        <!--  日期时间  -->
        <a-form-item v-else-if="canEdit(item.key) && item.type == 'DateTime'" :label="item.title" :name="item.key">
          <a-date-picker :defaultValue="data[item.key]" show-time @ok="onDateTimeOk($event,item)"/>
        </a-form-item>
        <!--  日期  -->
        <a-form-item v-else-if="canEdit(item.key) && item.type == 'Date'" :label="item.title" :name="item.key">
          <a-date-picker @ok="onDateTimeOk($event,item)"/>
        </a-form-item>
        <!--  上传文件  -->
        <a-form-item v-else-if="canEdit(item.key) && item.type == 'File'" :label="item.title" :name="item.key">
          <a-upload :before-upload="beforeUpload" :file-list="fileList[item.key]" @change="handleChange($event, item)">
            <a-button v-if="fileList.length < item.ext.fileNum">
              <upload-outlined></upload-outlined>
              上传
            </a-button>
          </a-upload>
        </a-form-item>
        <!--  上传图片 -->
        <a-form-item v-else-if="canEdit(item.key) && item.type == 'Image'" :label="item.title" :name="item.key">
          <a-upload :before-upload="beforeUpload" :customRequest="customRequest" :file-list="fileList[item.key]"
                    list-type="picture-card"
                    @change="handleChange($event, item)" @preview="handlePreview"
          >
            <div v-if="fileList[item.key].length < item.ext.fileNum">
              <plus-outlined/>
              <div class="ant-upload-text">上传</div>
            </div>
          </a-upload>

        </a-form-item>

        <template v-else>

        </template>

      </template>
    </a-form>
    <a-modal :footer="null" :visible="previewVisible" centered @cancel="previewCancel">
      <img :src="previewImage" style="width: 100%"/>
    </a-modal>
  </a-modal>
</template>
<script lang="ts">

import {Options, Vue} from 'vue-class-component';
import {AxiosResponse} from 'axios';
import {Moment} from 'moment';
import SysDictApi from "@/api/SysDictApi";

let dictApi = new SysDictApi();

export class ModalField {

  public key: string = '';
  public title: string = '';
  public type: "String" | "Select" | "Number" | "Slot" | "Password" | "DateTime" | 'File' | 'Image' | 'Textarea' | undefined;
  public slot: string | undefined;
  public edit: Boolean | undefined = true;
  public array: Array<Object> | undefined;
  public url: string | undefined;
  public ext: Ext | undefined = new Ext();

  public init(title: string, key: string, type: "String" | "Number" | "Slot" | "Password" | "DateTime" | 'File' | 'Image' | 'Textarea' | undefined, edit?: Boolean, ext?: Ext) {
    this.key = key;
    this.title = title;
    this.type = type;
    this.slot = key;
    if (ext != undefined) {
      this.ext = ext;
    }
    if (edit != undefined) {
      this.edit = edit;
    }
    return this
  }

  public initNumber(title: string, key: string, ext?: Ext) {
    this.key = key;
    this.title = title;
    this.type = "Number";
    this.ext = ext;
    return this
  }

  public initSelect(title: string, key: string, ext: Ext, array: SelectField[] | undefined, dictName?: string) {
    this.key = key;
    this.title = title;
    this.type = "Select";
    this.array = array;
    this.ext = ext;
    if (dictName) {
      this.getSelect(dictName);
    }
    return this
  }

  public getSelect(dictName: string) {
    SysDictApi.getSelect(dictName).then((res: AxiosResponse) => {
      this.array = res.data.data
    });
  }

}

export class SelectField {

  public lable: string | undefined;
  public value: any;

  constructor(lable: string, value: any) {
    this.lable = lable;
    this.value = value;
  }

}

export class Ext {
  name: string = "files";
  fileNum: number = 1;
  fileSize: number = 2;
}

@Options({
  name: 'FormModal',
  data() {
    return {
      url: "",
      visible: false,
      previewVisible: false,
      data: {},
      fileList: {},
      img: "",
      previewImage: "",
      layout: {
        labelCol: {span: 8},
        wrapperCol: {span: 14},
      },
    }
  },
  props: {
    title: String,
    getUrl: String,
    addUrl: String,
    editUrl: String,
    rules: {
      type: Object,
      default: () => {
        return {}
      }
    },
    columns: {
      type: Array,
      default: () => {
        return []
      }
    },
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
      for (let dataKey in this.data) {
        if (this.data[dataKey] != null) {
          formData.append(dataKey, this.data[dataKey])
        }
      }
      for (let column of this.columns) {
        if (column.type == "File" || column.type == "Image") {
          if (this.fileList[column.key].length > 0) {
            this.fileList[column.key].forEach((file: any) => {
              formData.append(column.ext.name, file.originFileObj as any);
            });
          }
        }
      }

      data = formData
      this.$refs.form.validate().then(() => {
        this.$axios({
          method: "post",
          url: this.url,
          data: data
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
    open(data: any) {
      this.visible = true
      if (!!data.id) {
        this.list(data.id)
        this.url = this.editUrl
      } else {
        this.url = this.addUrl
        this.data = data
      }
    },
    list(id: String) {
      this.$axios({
        method: "get",
        url: this.getUrl,
        params: {
          id: id
        }
      }).then((res: AxiosResponse) => {
        this.data = res.data.data
        for (let column of this.columns) {
          if (column.type == "File" || column.type == "Image") {
            if (!!this.data[column.key]) {
              this.fileList[column.key] = [{url: this.data[column.key]}]
            }
          }
        }
      });
    },
    getData() {
      return this.data
    },
    canEdit(key: string) {
      if (this.data.id == undefined) {
        return true
      }
      for (let column of this.columns) {
        if (column.key == key) {
          if (!column.edit) {
            return false
          }
        }
      }
      return true;
    },
    onDateTimeChange() {

    },
    onDateTimeOk(date: Moment, item: ModalField) {
      this.data[item.key] = date.toDate()
    },
    loadImage(file: any) {
      return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result as string);
        reader.onerror = error => reject(error);
      });
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
    handleChange(fileItem: any, item: ModalField) {
      let file = fileItem.file
      if (file.status == 'uploading') {
        if (!this.data[item.key]) {
          this.data[item.key] = []
        }
        file.status = 'done'
        // let a = await this.transformFile(file)
        // this.loadImage(file).then((base64: string) => {
        //   file.url = base64
        // });
        this.fileList[item.key] = [...this.fileList[item.key], file];
      }
      if (file.status == 'removed') {
        const index = this.fileList[item.key].indexOf(file);
        this.fileList[item.key].splice(index, 1);
      }
    },
    customRequest(file: any) {
      //不做任何实现不会自动上传
    },
    async transformFile(file: any) {
      return new Promise(resolve => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => {
          const canvas = document.createElement('canvas');
          const image: HTMLImageElement = document.createElement('img');
          image.src = reader.result as string;
          image.onload = () => {
            const ctx: CanvasRenderingContext2D = canvas.getContext('2d')!;
            if (image.width > 0 && image.height > 0) {
              if (image.width / image.height >= 164 / 112) {
                if (image.width > 164) {
                  image.width = 164;
                  image.height = (image.height * 164) / image.width;
                } else {
                  image.width = image.width;
                  image.height = image.height;
                }
                image.alt = image.width + "×" + image.height;
              } else {
                if (image.height > 112) {
                  image.height = 112;
                  image.width = (image.width * 112) / image.height;
                } else {
                  image.width = image.width;
                  image.height = image.height;
                }
                image.alt = image.width + "×" + image.height;
              }
            }
            ctx.drawImage(image, 0, 0);
            canvas.toBlob(resolve);
          };
        };
      });
    },
    async handlePreview(file: any) {
      if (!!file.url) {
        this.previewImage = file.url
      } else {
        this.previewImage = await this.loadImage(file.originFileObj)
      }
      this.previewVisible = true
    },
    previewCancel() {
      this.previewVisible = false
    }
  },
  created() {

  },
  beforeUpdate() {
    for (let column of this.columns) {
      if (column.type == "File" || column.type == "Image") {
        this.fileList[column.key] = []
      }
    }
  }

})


export default class FormModal extends Vue {
}

</script>