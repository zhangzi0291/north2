<style lang="less">

</style>
<template>
  <div>
    <base-page :breadcrumbs="breadcrumbs">
      <template #content>
        <a-page-header sub-title="区域配置" title="区域">
          <template #extra>
            <a-button type="primary" @click="openAdd()">新增</a-button>
            <a-button type="primary" @click="load({current:1})">查询</a-button>
          </template>
        </a-page-header>
        <a-row>
          <b>检索条件</b>
        </a-row>
        <a-row>
          <a-form layout="inline">
            <a-form-item label="区域名称">
              <a-input v-model:value="search.areaName" allowClear/>
            </a-form-item>
            <a-form-item label="区域级别">
              <a-select v-model:value="search.areaLevel" style="width:200px">
                <template v-for="select in areaLevels">
                  <a-select-option :value="select.value">
                    {{ select.lable }}
                  </a-select-option>
                </template>
              </a-select>
            </a-form-item>
            <a-form-item label="父区域码">
              <a-input v-model:value="search.parentId" allowClear/>
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
import SysAreaApi from "@/api/SysAreaApi";
import {AxiosResponse} from "axios";
import SysDictApi from "@/api/SysDictApi";

let api = new SysAreaApi();
let dictApi = new SysDictApi();

@Options({
  name: 'SysArea',
  components: {
    FormModal, MenuModal
  },
  data() {
    let areaLevels: never[] = [];
    SysDictApi.getSelect('区域级别').then((res: AxiosResponse) => {
      areaLevels = res.data.data
    });
    return {
      //表格加载状态
      loading: false,
      //接口url
      url: {
        get: "/sysArea/get",
        add: "/sysArea/add",
        edit: "/sysArea/edit",
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
      areaLevels:[],
      //表格字段
      columns: [
        {title: '区域码', key: 'id', dataIndex: 'id',sorter: true,},
        {title: '区域名称', key: 'areaName', dataIndex: 'areaName',},
        {
          title: '区域级别', key: 'areaLevel', dataIndex: 'data.resourceType',sorter: true,
          customRender: function (record: any) {
            for (let areaLevel  of areaLevels) {
              let type = (<any>areaLevel)
              if(type.value == record.record.areaLevel){
                return type.lable
              }
            }
            return "未知"
          }
        },
        {title: '城乡分类代码', key: 'townCode', dataIndex: 'townCode',sorter: true,},
        {title: '操作', dataIndex: 'operation', slots: {customRender: 'operation'}, fixed: 'right', width: "100px",},
      ],
      //form中的字段
      formColumns: [
        new ModalField().init('区域码', 'id', 'String'),
        new ModalField().init('区域名称', 'areaName', 'String'),
        new ModalField().initSelect('区域级别', 'areaLevel', new Ext(), [], '区域级别'),
        new ModalField().init('城乡分类代码', 'townCode', 'Number'),
        new ModalField().init('排序', 'areaOrder', 'Number'),
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
    loadAreaLevel(){
      SysDictApi.getSelect('区域级别').then((res: AxiosResponse) => {
        this.areaLevels = res.data.data
      });
    }
  },
  created() {
    this.load()
    this.loadAreaLevel()
  },
})

export default class SysArea extends Vue {
}

</script>