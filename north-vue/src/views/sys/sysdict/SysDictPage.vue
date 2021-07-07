<style lang="less">

</style>
<template>
  <div>
    <base-page :breadcrumbs="breadcrumbs">
      <template #content>
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
          <a-form layout="inline">
            <a-form-item label="字典名称">
              <a-input v-model:value="search.dictName" allowClear/>
            </a-form-item>

          </a-form>
        </a-row>
        <a-table :columns="columns" :data-source="data" :loading="loading" :rowKey="(record)=>record.id"
                 :scroll="{ x: 900, y: 210 }" :pagination="page" @change="tableChange"
                 bordered>
          <template #operation="{ record }">
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
        </a-table>
      </template>

    </base-page>
    <form-modal ref="form" :addUrl="url.add" :columns="formColumns" :editUrl="url.edit" :getUrl="url.get"
                :okCallback="load" :rules="rules" :title="'字典'">
      <template #resources="{data}">
        <a-button type="primary" @click="openMenuModal(data)">分配资源</a-button>
      </template>
    </form-modal>

  </div>
</template>
<script lang="ts">
import {Options, Vue} from 'vue-class-component';
import FormModal, {Ext, ModalField} from "@/components/base/FormModal.vue";
import MenuModal from "@/views/sys/sysrole/MenuModal.vue";
import {createVNode} from "vue";
import SysDictApi from "@/api/SysDictApi";

let api = new SysDictApi();

@Options({
  name: 'SysRole',
  components: {
    FormModal, MenuModal
  },
  data() {
    return {
      //表格加载状态
      loading: false,
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
      //查询数据
      search: {},
      //表格数据
      data: [],
      //分页
      page: {},
      //排序
      sort: {},
      //表格字段
      columns: [
        {title: '字典名称', key: 'dictName', dataIndex: 'dictName',sorter: true,},
        {title: '字典Lable', key: 'dictKey', dataIndex: 'dictKey',sorter: true,},
        {title: '字典Value', key: 'dictValue', dataIndex: 'dictValue',},
        {title: '描述', key: 'describe', dataIndex: 'describe',},
        {title: '操作', dataIndex: 'operation', slots: {customRender: 'operation'}, fixed: 'right', width: "100px",},
      ],
      //form中的字段
      formColumns: [
        new ModalField().init('字典名称', 'dictName', 'String'),
        new ModalField().init('字典Lable', 'dictKey', 'String'),
        new ModalField().init('字典Value', 'dictValue', 'String'),
        new ModalField().initSelect('值类型', 'valueType', new Ext(), [], '字典值类型'),
        new ModalField().init('排序', 'dictOrder', 'Number'),
        new ModalField().init('扩展字段', 'dictExt', 'Textarea'),
        new ModalField().init('描述', 'describe', 'Textarea'),
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
    load(data:any) {
      this.loading = true
      if(!!data && !!data.current ){
        this.page.current = data.current
      }
      api.list(this.search,this.page,this.sort).then(res => {
        this.data = res.data.data.records
        this.page = {
          current: res.data.data.current,
          total: res.data.data.total
        }
        this.loading = false
      })
    },
    tableChange(page:any, filters:any, sorter:any){
      this.page = page
      this.sort = {
        field:sorter.field,
        order:sorter.order
      }
      this.load()
    },
    openAdd(parentId: string) {
      this.$refs.form.open({parentId: parentId})
      this.check.roleName = undefined;
    },
    openEdit(id: string) {
      this.$refs.form.open({id: id})
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
          return api.del([id]).then(res => {
            this.load()
          })
        },
      });
    },
    openExport(){
      this.$download(this.url.export,"get","数据字典.xlsx",this.search)
    },
  },
  created() {
    this.load()
  },
})

export default class SysRole extends Vue {
}

</script>