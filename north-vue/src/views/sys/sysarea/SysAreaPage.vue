<style lang="less">

</style>
<template>
  <div>
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
      <a-form :layout="'inline'">
        <a-form-item label="区域名称">
          <a-input v-model:value="search.areaName" allowClear/>
        </a-form-item>
        <a-form-item label="区域级别">
          <a-select v-model:value="search.areaLevel" style="width:200px">
            <template v-for="select in areaLevels">
              <a-select-option v-model:value="select.value">
                {{ select.label }}
              </a-select-option>
            </template>
          </a-select>
        </a-form-item>
        <a-form-item label="父区域码">
          <a-input v-model:value="search.parentId" allowClear/>
        </a-form-item>
      </a-form>
    </a-row>
    <a-table :columns="columns" :data-source="data" :loading="loading" :pagination="page"
             :rowKey="(record)=>record.id" :scroll="tableScroll" bordered
             @change="tableChange">
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex == 'areaLevel'">
          <span>
            {{ getSelectLabel(areaLevels, record.areaLevel) }}
          </span>
        </template>
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
import {createVNode, defineComponent, onMounted, ref} from "vue";
import {AxiosResponse} from "axios";
import {PageInfo} from "@/base/Page";
import SysAreaApi from "@/api/sys/SysAreaApi";
import SysDictApi from "@/api/sys/SysDictApi";


export default defineComponent({
  name: 'SysArea',
  components: {
    FormModal
  },
  data() {
    return {
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
      //表格字段
      columns: [
        {title: '区域码', key: 'id', dataIndex: 'id', sorter: true,},
        {title: '区域名称', key: 'areaName', dataIndex: 'areaName',},
        {title: '区域级别', key: 'areaLevel', dataIndex: 'areaLevel', sorter: true,},
        {title: '城乡分类代码', key: 'townCode', dataIndex: 'townCode', sorter: true,},
        {title: '操作', dataIndex: 'operation', fixed: 'right', width: "100px",},
      ],
      //form中的字段
      formColumns: [
        ModalField.init('区域码', 'id', InputType.String),
        ModalField.init('区域名称', 'areaName', InputType.String),
        ModalField.init('区域级别', 'areaLevel', InputType.Select, <Ext>{selectParameter: {dictName: '区域级别'}}),
        ModalField.init('城乡分类代码', 'townCode', InputType.Number),
        ModalField.init('排序', 'areaOrder', InputType.Number),
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
          return SysAreaApi.del([id]).then(res => {
            this.load()
          })
        },
      });
    },
    loadAreaLevel() {
      SysDictApi.getSelect('区域级别').then((res: AxiosResponse) => {
        this.areaLevels = res.data.data
      });
    }
  },
  created() {
    this.load()
    this.loadAreaLevel()
  },
  setup() {
    const tablePageOption = PageInfo(SysAreaApi)

    let areaLevels = ref([])
    onMounted(async () => {
      const res = await SysDictApi.getSelect('区域级别')
      areaLevels.value = res.data.data
    });

    return {
      areaLevels,
      ...tablePageOption
    }
  }
})

</script>