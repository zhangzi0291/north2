<style lang="less">

</style>
<template>
  <div>
    <base-page :breadcrumbs="breadcrumbs">
      <template #content>
        <a-page-header sub-title="在线用户管理" title="用户">
          <template #extra>
            <a-button type="primary" @click="load()">刷新</a-button>
          </template>
        </a-page-header>

        <a-table :columns="columns" :data-source="data" :loading="loading" :rowKey="(record)=>record.id"
                 :scroll="{ x: 900, y: 500 }" :pagination="page" @change="tableChange"
                 bordered style="width: 100%">
          <template #iconUrl="{ record }">
            <a-avatar :src="record.iconUrl" size="large" style="background-color: #66ccff">
              <template v-if="1==1">{{ record.nickname }}</template>
            </a-avatar>
          </template>
          <template #operation="{ record }">
            <a-space>
              <a-tooltip title="用户下线">
                <a-button shape="circle" type="dashed" @click="kickUser(record.userId)">
                  <template #icon>
                    <LogoutOutlined/>
                  </template>
                </a-button>
              </a-tooltip>
            </a-space>
          </template>
        </a-table>
      </template>


    </base-page>

  </div>
</template>
<script lang="ts">
import {Options, Vue} from 'vue-class-component';
import SysUserOnlineApi from "@/api/SysUserOnlineApi";
import {createVNode, defineComponent, reactive, ref} from "vue";
import SysDictApi from "@/api/SysDictApi";
import {AxiosResponse} from "axios";


export default defineComponent({
  name: 'SysOnlineUser',
  components: {
  },
  data() {
    let loginDevices: never[] = [];
    SysDictApi.getSelect('登录设备').then((res: AxiosResponse) => {
      loginDevices = res.data.data
    });
    return {
      //面包屑
      breadcrumbs: [
        {name: "", icon: "HomeOutlined", href: "/home"},
        {name: "在线用户管理", icon: "UserOutlined", href: "/sysonlineuser/list"}
      ],
      //表格字段
      columns: [
        {title: '登录名', key: 'username', dataIndex: 'username'},
        {title: '用户名', key: 'nickname', dataIndex: 'nickname', ellipsis: "true"},
        {title: '登录设备', key: 'loginDevice', dataIndex: 'loginDevice', ellipsis: "true",
          customRender: function (record: any) {
            for (let dict  of loginDevices) {
              let type = (<any>dict)
              if(type.value == record.record.loginDevice){
                return type.lable
              }
            }
            return record.record.loginDevice
          }
        },
        {title: '头像', key: 'iconUrl', dataIndex: 'iconUrl', slots: {customRender: 'iconUrl'}},
        {title: '操作', dataIndex: 'operation', slots: {customRender: 'operation'}, fixed: 'right', width: "180px"},
      ],
    }
  },
  methods: {
    kickUser(id: string) {
      this.$modal.confirm({
        title: '用户下线',
        icon: createVNode(this.$icons["ExclamationCircleOutlined"]),
        content: '确定要踢用户下线吗？',
        onOk: () => {
          return SysUserOnlineApi.kickUser(id).then(res => {
            this.load()
          })
        },
      });
    }
  },
  created() {
    this.load()
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
      SysUserOnlineApi.list(search, page, sort).then(res => {
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