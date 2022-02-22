<style lang="less">

</style>
<template>
  <div>
    <base-page :breadcrumbs="breadcrumbs">
      <template #content>
        <a-page-header :sub-title="tableName" title="数据表">
          <template #extra>
            <a-button type="primary" @click="openImport()">导入</a-button>
            <a-button type="primary" @click="clearAll()">清空</a-button>
            <a-button type="primary" @click="load({current:1})">查询</a-button>
          </template>
        </a-page-header>
        <a-row>
          <b>检索条件</b>
        </a-row>
        <a-row>
          <a-form layout="inline">
            <a-form-item label="包含内容">
              <a-input v-model:value="search.search" allowClear/>
            </a-form-item>
          </a-form>
        </a-row>
        <a-table :columns="columns" :data-source="data" :loading="loading" :rowKey="(record)=>record.id"
                 :scroll="{ x: 900, y: 500 }" :pagination="page" @change="tableChange"
                 bordered style="width: 100%">
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

    <form-modal ref="form" :columns="formColumns" :addUrl="url.add" :editUrl="url.edit" :getUrl="url.get"
                :okCallback="load"
                :title="'新增数据'">
    </form-modal>

    <import-modal ref="importModal" :title="'数据导入'" :url="url.import" :okCallback="load"></import-modal>

  </div>
</template>
<script lang="ts">
import {createVNode, defineComponent, getCurrentInstance, reactive, ref} from "vue";
import JsonTableAPi from "@/api/JsonTable";
import FormModal, {ModalField} from "@/components/base/FormModal.vue";
import ImportModal from "@/components/base/ImportModal.vue";


export default defineComponent({
  name: 'JsonTableData',
  components: {
    FormModal, ImportModal
  },
  data() {
    return {
      //接口url
      url: {
        get: "/jsonTableValue/getTableValueById",
        add: "/jsonTableValue/addTableValue",
        edit: "/jsonTableValue/addTableValue",
        import: "/jsonTableValue/import?tableId=" + this.$route.params.id,
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
        new ModalField().init('表名', 'tableName', 'String'),
      ],
      //表格字段
      columns: [
        {title: 'ID', key: 'id', dataIndex: 'id'},
        {title: '表名', key: 'tableName', dataIndex: 'tableName', ellipsis: "true"},
        {title: '操作', dataIndex: 'operation', slots: {customRender: 'operation'}, fixed: 'right', width: "200px"},
      ],
      visible: false,
      mateData: [],
      tableId: "",
      columnType: [],
      tableName: ""
    }
  },
  methods: {
    openImport() {
      const importModal: any = this.$refs.importModal
      importModal.open()
    },
    openEdit(id: string) {
      const form: any = this.$refs.form
      form.open({id: id, tableId: this.tableId})
    },
    del(id: string) {
      this.$modal.confirm({
        title: '删除',
        icon: createVNode(this.$icons["ExclamationCircleOutlined"]),
        content: '确定要删除吗？',
        onOk: () => {
          return JsonTableAPi.delValue([id]).then(res => {
            this.load()
          })
        },
      });
    },
    clearAll() {
      console.log(this.tableId)
      this.$modal.confirm({
        title: '删除',
        icon: createVNode(this.$icons["ExclamationCircleOutlined"]),
        content: '确定清空吗，清空后不可恢复？',
        onOk: () => {
          return JsonTableAPi.delAllValue(this.tableId).then(res => {
            this.load()
          })
        },
      });
    },
    getTableInfo() {
      const tableId = <string>this.$route.params.id
      this.tableId = tableId
      JsonTableAPi.get(tableId).then(res => {
        const data = res.data.data
        this.tableName = data.tableName
      })
    },
    getColumnInfo() {
      const tableId = <string>this.$route.params.id
      JsonTableAPi.getMate(tableId).then(res => {
        const data = res.data.data
        const columnInfo = []
        data.forEach(d => {
          columnInfo.push({
            title: d.columnName,
            key: d.columnName,
            dataIndex: d.columnName,
          })
        })
        columnInfo.push({
          title: '操作',
          dataIndex: 'operation',
          slots: {customRender: 'operation'},
          fixed: 'right',
          width: "200px"
        })
        const formColumnsInfo = []
        data.forEach(d => {
          formColumnsInfo.push(new ModalField().init(d.columnName, d.columnName, d.columnType))
        })
        this.formColumns = formColumnsInfo
        this.columns = columnInfo
      })
    }
  },
  created() {
    this.load()
    this.getTableInfo()
    this.getColumnInfo()
  },
  setup() {
    const {proxy} = getCurrentInstance();

    //表格加载状态
    let loading = ref(false)
    //分页
    let page = reactive({current: 1, total: 0})
    //排序
    let sort = reactive({field: null, order: null})
    //查询数据
    let search = reactive({})
    //表格数据
    let data = ref([])

    const load = function (param?: any) {
      loading.value = true
      if (!!param && !!param.current) {
        page.current = param.current
      }

      const tableId = <string>proxy.$root.$route.params.id
      JsonTableAPi.valueList(tableId, search, page, sort).then(res => {
        data.value.length = 0
        data.value = data.value.concat(res.data.data.records)
        page.current = res.data.data.current
        page.total = res.data.data.total
        loading.value = false
      })
    }
    const tableChange = function (pageParam: any, filters: any, sorter: any) {
      page.current = pageParam.current
      page.total = pageParam.total
      sort.field = sorter.field
      sort.order = sorter.order
      load()
    }
    const tablePageOption = {loading, data, page, search, load, tableChange}

    return {
      ...tablePageOption
    }
  }
})

</script>