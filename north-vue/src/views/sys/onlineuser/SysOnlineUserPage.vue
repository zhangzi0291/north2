<style lang="less">

</style>
<template>
  <div>
    <a-page-header sub-title="在线用户管理" title="用户">
      <template #extra>
        <a-button type="primary" @click="load()">刷新</a-button>
      </template>
    </a-page-header>

    <a-table :columns="columns" :data-source="data" :loading="loading" :pagination="page"
             :rowKey="(record)=>record.id" :scroll="tableScroll" bordered
             style="width: 100%" @change="tableChange">
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'loginDevice'">
          <!--          {{ getSelectLabel(loginDevices, record.loginDevice) }}-->
          {{ record.loginDevice }}
        </template>
        <template v-if="column.dataIndex === 'iconUrl'">
          <a-avatar :src="record.iconUrl" size="large" style="background-color: #66ccff">
            <template v-if="1==1">{{ record.nickname }}</template>
          </a-avatar>
        </template>
        <template v-else-if="column.dataIndex === 'operation'">
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
      </template>
    </a-table>

  </div>
</template>
<script lang="ts">
import {createVNode, defineComponent} from "vue";
import {PageInfo} from "@/base/Page";
import SysUserOnlineApi from "@/api/sys/SysUserOnlineApi";

export default defineComponent({
  name: 'SysOnlineUser',
  components: {},
  data() {
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
        {title: '登录设备', key: 'loginDevice', dataIndex: 'loginDevice', ellipsis: "true",},
        {title: '头像', key: 'iconUrl', dataIndex: 'iconUrl'},
        {title: '操作', dataIndex: 'operation', fixed: 'right', width: "180px"},
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
          return SysUserOnlineApi.kickUser(id).then(() => {
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
    const tablePageOption = PageInfo(SysUserOnlineApi)

    // let loginDevices = ref([])
    // onMounted(async () => {
    //   const res = await SysDictApi.getSelect('登录设备')
    //   loginDevices.value = res.data.data
    // });

    return {
      // loginDevices,
      ...tablePageOption
    }
  }
})

</script>