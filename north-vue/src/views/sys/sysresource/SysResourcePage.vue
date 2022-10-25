<style lang="less">

</style>
<template>
  <div>
    <a-page-header sub-title="菜单按钮配置" title="资源树">
      <template #extra>
        <a-button type="primary" @click="load()">刷新</a-button>
        <a-button type="primary" @click="openAdd()">新增</a-button>
      </template>
    </a-page-header>
    <a-table :childrenColumnName="'child'"
             :columns="columns"
             :data-source="data"
             :defaultExpandAllRows="true"
             :loading="loading"
             :pagination="false"
             :rowKey="(record)=>record.id" :scroll="tableScroll" bordered>
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex == 'data.resourceName'">
          {{ record.data.resourceName }}
        </template>
        <template v-if="column.dataIndex === 'data.resourceIcon'">
          <span>
               <component :is="record.data.resourceIcon"/>
            </span>
        </template>
        <template v-if="column.dataIndex === 'data.resourceType'">
          <span>
            {{ getSelectLabel(resourceTypes, record.data.resourceType) }}
          </span>
        </template>
        <template v-if="column.dataIndex == 'data.resourceUrl'">
          {{ record.data.resourceUrl }}
        </template>
        <template v-if="column.dataIndex == 'data.describe'">
          {{ record.data.describe }}
        </template>
        <template v-if="column.dataIndex === 'operation'">
          <a-space>
            <a-tooltip title="添加子节点">
              <a-button shape="circle" type="dashed" @click="openAdd(record.id,record)">
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
      </template>
    </a-table>


    <form-modal ref="form" :addUrl="url.add" :columns="formColumns" :editUrl="url.edit" :getUrl="url.get"
                :init="formInit" :okCallback="load" :rules="rules"
                :title="'资源'">
      <template #parentId="{data}">
        <a-select v-model:value="data['parentId']" @click="openMenuModal(data)">
          <a-select-option v-model:value="parent.id">
            {{ parent.resourceName }}
          </a-select-option>
        </a-select>
      </template>
    </form-modal>

    <menu-modal ref="menuModal" :okCallback="menuModalOkCallback" :title="'父节点选择'" :type="'radio'"></menu-modal>
  </div>
</template>
<script lang="ts">

import {createVNode, defineComponent, onMounted, ref} from 'vue';

import FormModal, {Ext, InputType, ModalField, SelectField} from '@/components/base/FormModal.vue'
import MenuModal from "@/views/sys/sysresource/MenuModal.vue";
import {AxiosResponse} from "axios";
import SysDictApi from "@/api/sys/SysDictApi";
import SysResourceApi from "@/api/sys/SysResourceApi";

export default defineComponent({
  name: 'SysResource',
  components: {
    FormModal, MenuModal
  },
  data() {
    return {
      //表格加载状态
      loading: false,
      //接口url
      url: {
        get: "/sysResource/getByParentName",
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
      parent: {},
      //表格字段
      columns: [
        {title: '资源名称', key: 'data.resourceName', dataIndex: 'data.resourceName',},
        {title: '资源ICON', key: 'data.resourceIcon', dataIndex: 'data.resourceIcon'},
        {title: '资源类型', key: 'data.resourceType', dataIndex: 'data.resourceType',},
        {title: '资源路径', key: 'data.resourceUrl', dataIndex: 'data.resourceUrl',},
        {title: '描述', key: 'data.describe', dataIndex: 'data.describe',},
        {title: '操作', dataIndex: 'operation', fixed: 'right', width: "150px",},
      ],
      //form中的字段
      formColumns: [
        ModalField.init('资源名称', 'resourceName', InputType.String),
        ModalField.init('资源ICON', 'resourceIcon', InputType.String),
        ModalField.init('资源类型', 'resourceType', InputType.Select, <Ext>{
          selectParameter: {
            array: [new SelectField("菜单", "1"), new SelectField("资源", "2")],
            dictName: '资源类型'
          }
        }),
        ModalField.init('资源路径', 'resourceUrl', InputType.String),
        ModalField.init('父资源ID', 'parentId', InputType.Slot),
        ModalField.init('排序', 'resourceOrder', InputType.Number),
        ModalField.init('描述', 'describe', InputType.String),
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
              SysResourceApi.checkResourceName(value, originalValue).then(res => {
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
        resourceType: [{required: true, type: 'number', trigger: 'blur', message: "资源类型不可为空"}],
        parentId: [{required: true, type: 'string', trigger: 'blur', message: "父资源ID不可为空"}],
      },
      check: {
        resourceName: ""
      },
    }
  },
  methods: {
    load() {
      this.loading = true
      SysResourceApi.getAllResource().then(res => {
        this.data = res.data.data
        this.loading = false
      })
    },
    openAdd(parentId: string, data: any) {
      this.$refs.form.open({parentId: parentId})
    },
    async openEdit(id: string) {
      await this.$refs.form.open({id: id})
      let data = this.$refs.form.getData()
      this.check.resourceName = data.resourceName;
      this.parent = {
        resourceName: data.parentName,
        id: data.parentId,
      }
    },
    formInit() {
      let data = this.$refs.form.getData()
      if (data.parentId && data.parentId != -1) {
        SysResourceApi.getResource(data.parentId).then((res: AxiosResponse) => {
          let d = res.data.data;
          this.parent = {
            resourceName: d.resourceName,
            id: d.id,
          }
          console.log(this.parent)
        })
      }
    },
    openMenuModal(data: any) {
      this.$refs.menuModal.open(data)
    },
    menuModalOkCallback(data: any, selectData: any) {
      this.parent = selectData[0]
    },
    del(id: string) {
      this.$modal.confirm({
        title: '删除',
        icon: createVNode(this.$icons["ExclamationCircleOutlined"]),
        content: '确定要删除吗？',
        onOk: () => {
          return SysResourceApi.del([id]).then(res => {
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
    let resourceTypes = ref([])
    onMounted(async () => {
      const res = await SysDictApi.getSelect('资源类型')
      resourceTypes.value = res.data.data
    });


    return {resourceTypes}
  }
})

</script>