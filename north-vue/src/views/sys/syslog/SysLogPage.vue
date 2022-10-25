<style lang="less">

</style>
<template>
  <div>
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
      <a-form :layout="'inline'">
        <a-form-item label="用户名">
          <a-input v-model:value="search.nickname" allowClear/>
        </a-form-item>
        <a-form-item label="日志类型">
          <a-select v-model:value="search.logType" allowClear style="width:200px">
            <template v-for="select in logTypes">
              <a-select-option v-model:value="select.value">
                {{ select.label }}
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
    <a-table :columns="columns" :data-source="data" :loading="loading" :pagination="page"
             :rowKey="(record)=>record.id" :scroll="tableScroll" bordered
             @change="tableChange">
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex == 'logType'">
          <span>
            {{ getSelectLabel(logTypes, record.logType) }}
          </span>
        </template>
      </template>
    </a-table>
  </div>
</template>
<script lang="ts">
import {AxiosResponse} from "axios";
import {defineComponent, onMounted, ref} from "vue";
import {PageInfo} from "@/base/Page";
import SysLogApi from "@/api/sys/SysLogApi";
import SysDictApi from "@/api/sys/SysDictApi";


export default defineComponent({
  name: 'SysLog',
  data() {
    return {
      //面包屑
      breadcrumbs: [
        {name: "", icon: "HomeOutlined", href: "/home"},
        {name: "角色管理", icon: "KeyOutlined", href: "/sysrole/list"}
      ],
      //表格字段
      columns: [
        {title: '用户名', key: 'nickname ', dataIndex: 'nickname',},
        {title: 'IP', key: 'ipAddr ', dataIndex: 'ipAddr',},
        {title: '模块名称', key: 'moduleName ', dataIndex: 'moduleName',},
        {title: '操作名称', key: 'operationName ', dataIndex: 'operationName',},
        {title: '日志类型', key: 'logType', dataIndex: 'logType',},
        {title: '操作时间', key: 'createdTime', dataIndex: 'createdTime',},
        {title: '类名', key: 'className ', dataIndex: 'className',},
        {title: '方法名', key: 'methodName ', dataIndex: 'methodName',},
        {title: '备注', key: 'remakr', dataIndex: 'remakr',},
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
    const tablePageOption = PageInfo(SysLogApi)

    let logTypes = ref([])
    onMounted(async () => {
      const res = await SysDictApi.getSelect('日志类型')
      logTypes.value = res.data.data
    });

    return {
      logTypes,
      ...tablePageOption
    }
  }
})

</script>