<style lang="less">

</style>
<template>
  <a-modal :onCancel="cancel" :onOk="ok" :title="title" :visible="visible">
    <a-form ref="form" :model="data" :rules="rules" v-bind="layout">
      <a-form-item v-for="item in columnsComputed" :key="item.key+item.title" :label="item.title" :name="item.key">
        <!--  插槽  -->
        <template v-if="item.type == 'Slot'">
          <slot :data="data" :name="item.slot"/>
        </template>
        <!--  字符串  -->
        <template v-else-if="item.type == 'String'" :label="item.title" :name="item.key">
          <a-input v-model:value.sync="data[item.key]" allowClear/>
        </template>
        <!--  文本框  -->
        <template v-else-if="item.type == 'Textarea'" :label="item.title" :name="item.key">
          <a-textarea v-model:value="data[item.key]" allowClear/>
        </template>
        <!--  密码  -->
        <template v-else-if="item.type == 'Password'" :label="item.title" :name="item.key">
          <a-input-password v-model:value="data[item.key]"/>
        </template>
        <!--  数字  -->
        <template v-else-if="item.type == 'Number'" :label="item.title" :name="item.key">
          <a-input-number v-model:value="data[item.key]">
            {{ data[item.key] }}
          </a-input-number>
        </template>
        <!--  下拉框  -->
        <template v-else-if="item.type == 'Select'" :label="item.title" :name="item.key">
          <a-select v-model:value="data[item.key]">
            <template v-for="select in item.array">
              <a-select-option v-model:value="select.value">
                {{ select.label }}
              </a-select-option>
            </template>
          </a-select>
        </template>
        <!--  日期时间  -->
        <template v-else-if="item.type == 'DateTime'" :label="item.title" :name="item.key">
          <a-date-picker v-model:value="data[item.key]" show-time valueFormat="YYYY-MM-DD HH:mm:ss"/>
        </template>
        <!--  日期  -->
        <template v-else-if="item.type == 'Date'" :label="item.title" :name="item.key">
          <a-date-picker v-model:value="data[item.key]"/>
        </template>
        <!--  上传文件  -->
        <template v-else-if="item.type == 'File'" :label="item.title" :name="item.key">
          <upload-file :init-file-list="fileList[item.key]" @fileChange="fileChange($event,item.key)"></upload-file>
        </template>
        <!--  上传图片 -->
        <template v-else-if="item.type == 'Image'" :label="item.title" :name="item.key">
          <upload-image :init-file-list="fileList[item.key]" @imageChange="imageChange($event,item.key)"></upload-image>
        </template>

        <template v-else>

        </template>

      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script lang="ts">
import {AxiosResponse} from 'axios';
import {Dayjs} from 'dayjs';
import UploadImage from "@/components/UploadImage.vue";
import locale from 'ant-design-vue/es/date-picker/locale/zh_CN';
import {defineComponent} from "vue";
import SysDictApi from '@/api/sys/SysDictApi';
import UploadFile from "@/components/UploadFile.vue";

export class SelectField {

  public label: string | undefined;
  public value: any;

  constructor(label: string, value: any) {
    this.label = label;
    this.value = value;
  }

  public static init(label: string, value: any) {
    return new SelectField(label, value)
  }

}

export class Ext {
  name: string = "files";
  edit: boolean = true;
  selectParameter: SelectParameter;
  fileNum: number = 1;
  fileSize: number = 2;
}

export enum InputType {
  String = "String",
  Select = "Select",
  Number = "Number",
  Password = "Password",
  DateTime = "DateTime",
  Date = "Date",
  File = "File",
  Image = "Image",
  Textarea = "Textarea",
  Slot = "Slot",
}

class SelectParameter {
  array: SelectField[] = [];
  dictName: string;
}

export class ModalField {


  public key: string = '';
  public title: string = '';
  public type: InputType;
  public slot: string | undefined;
  public edit: Boolean | undefined = true;
  public array: Array<Object> | undefined;
  public url: string | undefined;
  public ext: Ext = new Ext();

  public static init(title: string, key: string, type: InputType, ext?: Ext) {
    const modalField = new ModalField()
    modalField.key = key;
    modalField.title = title;
    modalField.type = type;
    modalField.slot = key;
    if (ext == undefined) {
      ext = new Ext();
    }
    if (ext.edit != undefined) {
      modalField.edit = ext.edit;
    }
    if (ext.edit != undefined) {
      modalField.edit = ext.edit;
    }
    if (ext.selectParameter != undefined) {
      if (ext.selectParameter.array) {
        modalField.array = ext.selectParameter.array
      }
      if (ext.selectParameter.dictName) {
        modalField.getSelect(ext.selectParameter.dictName);
      }
    }

    return modalField
  }

  public init(title: string, key: string, type: InputType, edit?: Boolean, ext?: Ext) {
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

  public getSelect(dictName: string) {
    SysDictApi.getSelect(dictName).then((res: AxiosResponse) => {
      this.array = res.data.data
    });
  }

}


export default defineComponent({
  name: 'FormModal',
  components: {UploadImage, UploadFile},
  data() {
    return {
      url: "",
      visible: false,
      data: {},
      fileList: {},
      img: "",
      isEdit: false,
      layout: {
        labelCol: {span: 8},
        wrapperCol: {span: 14},
      },
    }
  },
  computed: {
    columnsComputed: function () {
      return this.columns.filter(column=> !this.data.id || column.edit )
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
    init: {
      type: Function,
      default: () => {
        // console.log("init")
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
    reload() {
      this.$forceUpdate()
    },
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
          if (this.fileList[column.key] != null && this.fileList[column.key].length > 0) {
            this.fileList[column.key].forEach((file: any) => {
              formData.append(column.ext.name, file.originFileObj as any);
            });
          }
        }
      }

      data = formData
      this.$refs.form.validate().then(() => {
        this.$http.request({
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
    async open(data: any) {
      this.visible = true
      if (data == undefined) {
        data = {}
      }
      if (data.id) {
        await this.list(data.id)
        for (let columnsKey in this.columns) {
          if (this.columns[columnsKey].type == 'Image') {
            let key = this.columns[columnsKey].key
            let value = this.data[this.columns[columnsKey].key]
            this.fileList[key] = [{url: value}]
          }
        }
        this.url = this.editUrl
        this.isEdit = true
      } else {
        this.url = this.addUrl
        this.data = data
        this.isEdit = false
      }
      this.init()
    },
    async list(id: String) {
      await this.$http.request({
        method: "get",
        url: this.getUrl,
        params: {
          id: id
        }
      }).then((res: AxiosResponse) => {
        this.data = res.data.data
        for (let column of this.columns) {
          if (column.type == "File" || column.type == "Image") {
            if (this.data[column.key]) {
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
    onDateTimeOk(date: Dayjs, item: ModalField) {
      this.data[item.key] = date.toDate().Format('yyyy-MM-dd hh:mm:ss')
    },
    imageChange(fileList, key) {
      this.fileList[key] = fileList
    },
    fileChange(fileList, key) {
      this.fileList[key] = fileList
    },
    validate() {
      this.$refs.form.validate()
    }
  },
  created() {

  },
  setup() {
    return {
      locale,
    };
  },
})

</script>