<style lang="less">

</style>
<template>
  <div>
    <a-page-header sub-title="字典配置" title="字典">
      <template #extra>
        <a-button type="primary" @click="openExport()">导出</a-button>
        <a-button type="primary" @click="openAdd()">新增</a-button>
        <a-button type="primary" @click="load({current:1})">查询</a-button>
      </template>
    </a-page-header>
    <a-row>
      <b>检索条件</b>
    </a-row>
    <a-row>
      <a-form :layout="'inline'">
        <a-form-item label="字典名称">
          <a-input v-model:value="search.dictName" allowClear/>
        </a-form-item>

      </a-form>
    </a-row>
    <a-table :columns="columns" :data-source="data" :loading="loading" :pagination="page"
             :rowKey="(record)=>record.id" :scroll="tableScroll" bordered
             @change="tableChange">
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'operation'">
          <a-space>
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
                :okCallback="load" :rules="rules" :title="'字典'">
      <template #resources="{data}">
        <a-button type="primary" @click="openMenuModal(data)">分配资源</a-button>
      </template>
    </form-modal>

  </div>
</template>
<script lang="ts">
import FormModal, {Ext, InputType, ModalField} from "@/components/base/FormModal.vue";
import {createVNode, defineComponent,} from "vue";
import {PageInfo} from "@/base/Page";
import SysDictApi from "@/api/sys/SysDictApi";

export default defineComponent({
  name: 'SysDict',
  components: {
    FormModal,
  },
  data() {
    return {
      //接口url
      url: {
        get: "/sysDict/get",
        add: "/sysDict/add",
        edit: "/sysDict/edit",
        export: "/sysDict/export",
      },
      //面包屑
      breadcrumbs: [
        {name: "", icon: "HomeOutlined", href: "/home"},
        {name: "字典管理", icon: "KeyOutlined", href: "/sysrole/list"}
      ],
      //表格字段
      columns: [
        {title: '字典名称', key: 'dictName', dataIndex: 'dictName', sorter: true,},
        {title: '字典label', key: 'dictKey', dataIndex: 'dictKey', sorter: true,},
        {title: '字典Value', key: 'dictValue', dataIndex: 'dictValue',},
        {title: '描述', key: 'describe', dataIndex: 'describe',},
        {title: '操作', dataIndex: 'operation', fixed: 'right', width: "100px",},
      ],
      //form中的字段
      formColumns: [
        ModalField.init('字典名称', 'dictName', InputType.String),
        ModalField.init('字典label', 'dictKey', InputType.String),
        ModalField.init('字典Value', 'dictValue', InputType.String),
        ModalField.init('值类型', 'valueType', InputType.Select, <Ext>{selectParameter: {dictName: '字典值类型'}}),
        ModalField.init('排序', 'dictOrder', InputType.Number),
        ModalField.init('扩展字段', 'dictExt', InputType.Textarea),
        ModalField.init('描述', 'describe', InputType.Textarea),
      ],
      //fomr校验规则
      rules: {
        dictName: [
          {required: true, type: 'string', trigger: 'change', message: "字典名称不可为空"},
        ],
        dictKey: [
          {required: true, type: 'string', trigger: 'change', message: "字典KEY不可为空"},
        ],
        dictValue: [
          {required: true, type: 'string', trigger: 'change', message: "字典值不可为空"},
        ],
      },
      //校验的原始值
      check: {
        roleName: "",
      }
    }
  },
  methods: {
    openAdd(parentId: string) {
      const form: any = this.$refs.form
      form.open({parentId: parentId})
      const check: any = this.check
      check.roleName = undefined;
    },
    openEdit(id: string) {
      const form: any = this.$refs.form
      form.open({id: id})
      // setTimeout(() => {
      //   this.check.roleName = this.$refs.form.getData().roleName;
      // }, 1000)
    },
    del(id: string) {
      this.$modal.confirm({
        title: '删除',
        icon: createVNode(this.$icons["ExclamationCircleOutlined"]),
        content: '确定要删除吗？',
        onOk: () => {
          return SysDictApi.del([id]).then(() => {
            this.load()
          })
        },
      });
    },
    openExport() {
      this.$download(this.url.export, "get", "数据字典.xlsx", this.search)
    },
  },
  created() {
    this.load()
  },
  setup() {
    const tablePageOption = PageInfo(SysDictApi)

    return {
      ...tablePageOption
    }
  }
})
</script>