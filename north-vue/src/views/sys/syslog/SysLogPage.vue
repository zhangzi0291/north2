<style lang="less">

</style>
<template>
  <div>
    <base-page :breadcrumbs="breadcrumbs">
      <template #content>
        <a-page-header sub-title="日志管理" title="日志">
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
            <a-form-item label="用户名">
              <a-input v-model:value="search.nickname" allowClear/>
            </a-form-item>
            <a-form-item label="日志类型">
              <a-select v-model:value="search.logType" style="width:200px" allowClear>
                <template v-for="select in logTypes">
                  <a-select-option :value="select.value">
                    {{ select.lable }}
                  </a-select-option>
                </template>
              </a-select>
            </a-form-item>
            <a-form-item label="模块名称">
              <a-input v-model:value="search.moduleName" allowClear/>
            </a-form-item>
            <a-form-item label="操作名称">
              <a-input v-model:value="search.operationName" allowClear/>
            </a-form-item>
          </a-form>
        </a-row>
        <a-table :columns="columns" :data-source="data" :loading="loading" :rowKey="(record)=>record.id"
                 :scroll="{ x: 900, y: 500 }" :pagination="page" @change="tableChange"
                 bordered>
          <!--          <template #operation="{ record }">-->
          <!--            <a-space>-->
          <!--              <a-tooltip title="删除">-->
          <!--                <a-button shape="circle" type="dashed" @click="del(record.id)">-->
          <!--                  <template #icon>-->
          <!--                    <DeleteOutlined/>-->
          <!--                  </template>-->
          <!--                </a-button>-->
          <!--              </a-tooltip>-->
          <!--            </a-space>-->
          <!--          </template>-->
        </a-table>
      </template>

    </base-page>


  </div>
</template>
<script lang="ts">
import SysLogApi from '@/api/SysLogApi'
import {AxiosResponse} from "axios";
import SysDictApi from "@/api/SysDictApi";
import {defineComponent, reactive, ref} from "vue";


export default defineComponent({
  name: 'SysLog',
  data() {
    let logTypes = [];
    SysDictApi.getSelect('日志类型').then((res: AxiosResponse) => {
      logTypes = res.data.data
    });
    return {
      //面包屑
      breadcrumbs: [
        {name: "", icon: "HomeOutlined", href: "/home"},
        {name: "角色管理", icon: "KeyOutlined", href: "/sysrole/list"}
      ],
      logTypes: [],
      //表格字段
      columns: [
        {title: '用户名', key: 'nickname ', dataIndex: 'nickname',},
        {title: 'IP', key: 'ipAddr ', dataIndex: 'ipAddr',},
        {title: '模块名称', key: 'moduleName ', dataIndex: 'moduleName',},
        {title: '操作名称', key: 'operationName ', dataIndex: 'operationName',},
        {
          title: '日志类型', key: 'logType  ', dataIndex: 'logType',
          customRender: function (record: any) {
            for (let logType of logTypes) {
              let type = (<any>logType)
              if (type.value == record.record.logType) {
                return type.lable
              }
            }
            return "未知"
          }
        },
        {title: '操作时间', key: 'createdTime', dataIndex: 'createdTime',},
        {title: '类名', key: 'className ', dataIndex: 'className',},
        {title: '方法名', key: 'methodName ', dataIndex: 'methodName',},
        {title: '备注', key: 'remakr', dataIndex: 'remakr',},
        // {title: '操作', dataIndex: 'operation', slots: {customRender: 'operation'}, fixed: 'right', width: "150px",},
      ],
    }
  },
  methods: {
    loadLogType() {
      SysDictApi.getSelect('日志类型').then((res: AxiosResponse) => {
        this.logTypes = res.data.data
      });
    },

  },
  created() {
    this.load()
    this.loadLogType()
  },
  setup() {
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
      SysLogApi.list(search, page, sort).then(res => {
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