<style lang="less">

</style>
<template>
  <div>
    <a-page-header sub-title="数据表元数据" title="">
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
        <a-form-item label="用户名">
          <a-input v-model:value="search.nickname" allowClear/>
        </a-form-item>
      </a-form>
    </a-row>
    <a-table :columns="columns" :data-source="data" :loading="loading" :pagination="page"
             :rowKey="(record)=>record.id" :scroll="tableScroll" bordered
             style="width: 100%" @change="tableChange">
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
            <a-tooltip title="元数据">
              <a-button shape="circle" type="dashed" @click="openMetaEdit(record.id)">
                <template #icon>
                  <TableOutlined/>
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
                :okCallback="load"
                :title="'用户'">
    </form-modal>

    <a-modal :onCancel="onCancel" :onOk="onOk" :title="'编辑元数据'" :visible="visible">
      <a-form ref="metaform" :model="mateData">
        <a-form-item v-for="(mate,index) in mateData" :key="mate.id">
          <a-space>
            <a-input v-show="false" v-model:value="mate.id" placeholder="id" style="width:230px"/>
            <a-input v-model:value="mate.columnName" placeholder="字段名称" style="width:150px"/>
            <a-select v-model:value="mate.columnType" placeholder="字段类型" style="width:100px">
              <template v-for="select in columnType">
                <a-select-option v-model:value="select.value">
                  {{ select.label }}
                </a-select-option>
              </template>
            </a-select>
            <a-input v-model:value="mate.orderNum" placeholder="序号" style="width:100px"/>
            <a-button type="text" @click="mateData.splice(index, 1);">
              <MinusCircleOutlined :style="{fontSize: '24px'}"/>
            </a-button>
          </a-space>
        </a-form-item>
      </a-form>
      <a-button type="text" @click="mateData.push({})">
        <PlusCircleOutlined :style="{fontSize: '24px'}"/>
      </a-button>

    </a-modal>

  </div>
</template>
<script lang="ts">
import {createVNode, defineComponent} from "vue";
import FormModal, {InputType, ModalField} from "@/components/base/FormModal.vue";
import {AxiosResponse} from "axios";
import JsonTableApi from "@/api/JsonTableApi";
import {PageInfo} from "@/base/Page";
import SysDictApi from "@/api/sys/SysDictApi";

export default defineComponent({
  name: 'JsonTable',
  components: {
    FormModal
  },
  data() {
    return {
      //接口url
      url: {
        get: "/jsonTable/get",
        add: "/jsonTable/add",
        edit: "/jsonTable/edit",
      },
      //面包屑
      breadcrumbs: [
        {name: "", icon: "HomeOutlined", href: "/home"},
        {name: "数据表元数据管理", icon: "UserOutlined", href: "/sys/jsontable"}
      ],
      //查询数据
      search: {},
      //表格数据
      data: [],
      //form中的字段
      formColumns: [
        ModalField.init('表名', 'tableName', InputType.String),
      ],
      //表格字段
      columns: [
        {title: 'ID', key: 'id', dataIndex: 'id'},
        {title: '表名', key: 'tableName', dataIndex: 'tableName', ellipsis: "true"},
        {title: '操作', dataIndex: 'operation', fixed: 'right', width: "200px"},
      ],
      visible: false,
      mateData: [],
      tableId: "",
      columnType: [],
    }
  },
  methods: {
    loadDict() {
      SysDictApi.getSelect('元数据数据类型').then((res: AxiosResponse) => {
        this.columnType = res.data.data
      });
    },
    openAdd(parentId: string) {
      const form: any = this.$refs.form
      form.open({parentId: parentId})
    },
    openEdit(id: string) {
      const form: any = this.$refs.form
      form.open({id: id})
    },
    openMetaEdit(tableId: string) {
      this.visible = true
      this.tableId = tableId
      JsonTableApi.JsonTableAPi.getMate(tableId).then(res => {
        this.mateData = res.data.data
      })
    },
    del(id: string) {
      this.$modal.confirm({
        title: '删除',
        icon: createVNode(this.$icons["ExclamationCircleOutlined"]),
        content: '确定要删除吗？',
        onOk: () => {
          return JsonTableApi.JsonTableAPi.del([id]).then(res => {
            this.load()
          })
        },
      });
    },
    onOk() {
      JsonTableApi.JsonTableAPi.addMate(this.tableId, this.mateData).then(res => {
        this.visible = false
      })
    },
    onCancel() {
      this.visible = false
    },

  },
  created() {
    this.load()
    this.loadDict()
  },
  setup() {
    const tablePageOption = PageInfo(JsonTableApi.JsonTableAPi)

    return {
      ...tablePageOption
    }
  }
})

</script>