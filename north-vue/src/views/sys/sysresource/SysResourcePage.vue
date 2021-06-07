<style lang="less">

</style>
<template>
  <div>
    <base-page :breadcrumbs="breadcrumbs">
      <template #content>
        <a-page-header sub-title="菜单按钮配置" title="资源树">
          <template #extra>
            <a-button type="primary" @click="load()">刷新</a-button>
            <a-button type="primary" @click="openAdd()">新增</a-button>
          </template>
        </a-page-header>
        <a-table :childrenColumnName="'child'" :columns="columns" :data-source="data" :defaultExpandAllRows="true"
                 :loading="loading"
                 :pagination="false"
                 :rowKey="(record)=>record.id" :scroll="{ x: 900, y: 500 }" bordered>
          <template #icon="{ text }">
            <span>
               <component :is="$icons[text]"/>
            </span>
          </template>
          <template #operation="{ record }">
            <a-space>
              <a-tooltip title="添加子节点">
                <a-button shape="circle" type="dashed" @click="openAdd(record.id)">
                  <template #icon>
                    <PlusOutlined/>
                  </template>
                </a-button>
              </a-tooltip>
              <a-tooltip title="编辑">
                <a-button shape="circle" type="dashed" @click="openEdit(record.id)">
                  <template #icon>
                    <EditOutlined/>
                  </template>
                </a-button>
              </a-tooltip>
              <a-tooltip title="删除">
                <a-button shape="circle" type="dashed" @click="del(record.id)">
                  <template #icon>
                    <DeleteOutlined/>
                  </template>
                </a-button>
              </a-tooltip>
            </a-space>
          </template>
        </a-table>

      </template>

    </base-page>

    <form-modal ref="form" :addUrl="url.add" :columns="formColumns" :editUrl="url.edit" :getUrl="url.get"
                :okCallback="load" :rules="rules"
                :title="'资源'">
      <template #parentId="{data}">
        <a-input v-model:value="data['parentId']" @click="openMenuModal(data)"/>
      </template>
    </form-modal>

    <menu-modal ref="menuModal" :title="'父节点选择'" :type="'radio'"></menu-modal>
  </div>
</template>
<script lang="ts">

import {Options, Vue} from 'vue-class-component';
import {createVNode} from 'vue';

import SysResourceApi from '@/api/SysResourceApi'
import FormModal, {Ext, ModalField, SelectField} from '@/components/base/FormModal.vue'
import MenuModal from "@/views/sys/sysresource/MenuModal.vue";
import {AxiosResponse} from "axios";
import SysDictApi from "@/api/SysDictApi";

let api = new SysResourceApi()
let dictApi = new SysDictApi();

@Options({
  name: 'SysResource',
  components: {
    FormModal, MenuModal
  },
  data() {
    let resourceTypes: never[] = [];
    SysDictApi.getSelect('资源类型').then((res: AxiosResponse) => {
      resourceTypes = res.data.data
    });
    return {
      //表格加载状态
      loading: false,
      //接口url
      url: {
        get: "/sysResource/get",
        add: "/sysResource/add",
        edit: "/sysResource/edit",
      },
      //面包屑
      breadcrumbs: [
        {name: "", icon: "HomeOutlined", href: "/home"},
        {name: "资源管理", icon: "MenuOutlined", href: "/sysresource/list"}
      ],
      //表格数据
      data: [],
      //表格字段
      columns: [
        {title: '资源名称', key: 'data.resourceName', dataIndex: 'data.resourceName',},
        {title: '资源ICON', key: 'data.resourceIcon', dataIndex: 'data.resourceIcon', slots: {customRender: "icon"},},
        {
          title: '资源类型', key: 'data.resourceType', dataIndex: 'data.resourceType',
          customRender: function (record: any) {
            for (let resourceType  of resourceTypes) {
              let type = (<any>resourceType)
              if(type.value == record.record.data.resourceType){
                return type.lable
              }
            }
            return record.record.data.resourceType == 1 ? "菜单" : "资源"
          }
        },
        {title: '资源路径', key: 'data.resourceUrl', dataIndex: 'data.resourceUrl',},
        {title: '描述', key: 'data.describe', dataIndex: 'data.describe',},
        {title: '操作', dataIndex: 'operation', slots: {customRender: 'operation'}, fixed: 'right', width: "150px",},
      ],
      //form中的字段
      formColumns: [
        new ModalField().init('资源名称', 'resourceName', 'String'),
        new ModalField().init('资源ICON', 'resourceIcon', 'String'),
        new ModalField().initSelect('资源类型', 'resourceType', new Ext(), [new SelectField("菜单", "1"), new SelectField("资源", "2")],"资源类型"),
        new ModalField().init('资源路径', 'resourceUrl', 'String'),
        new ModalField().init('父资源ID', 'parentId', "Slot"),
        new ModalField().init('排序', 'resourceOrder', 'Number'),
        new ModalField().init('描述', 'describe', 'String'),
      ],
      //fomr校验规则
      rules: {
        resourceName: [
          {required: true, type: 'string', trigger: 'blur', message: "资源名称不可为空"},
          {
            validator: (rule: any, value: any, callback: any) => {
              if (!value) {
                return callback()
              }
              let originalValue = (<any>this.check).resourceName;
              api.checkResourceName(value, originalValue).then(res => {
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
        resourceType: [{required: true, type: 'number', trigger: 'blur', message: "资源类型不可为空"}],
        parentId: [{required: true, type: 'string', trigger: 'blur', message: "父资源ID不可为空"}],
      },
      check:{
        resourceName:""
      },
      resourceTypes:[]
    }
  },
  methods: {
    load() {
      this.loading = true
      api.getAllResource().then(res => {
        this.data = res.data.data
        this.loading = false
      })
    },
    openAdd(parentId: string) {
      this.$refs.form.open({parentId: parentId})
      this.check.resourceName = undefined;
    },
    openEdit(id: string) {
      this.$refs.form.open({id: id})
      setTimeout(() => {
        this.check.resourceName = this.$refs.form.getData().resourceName;
      }, 1000)
    },
    openMenuModal(data: any) {
      this.$refs.menuModal.open(data)
    },
    del(id: string) {
      this.$modal.confirm({
        title: '删除',
        icon: createVNode(this.$icons["ExclamationCircleOutlined"]),
        content: '确定要删除吗？',
        onOk: () => {
          return api.del([id]).then(res => {
            this.load()
          })
        },
      });
    },
  },
  created() {
    this.load()
  },
  mounted() {
  },
  setup() {
  }
})

export default class SysResource extends Vue {
}

</script>