<style lang="less">

</style>
<template>
  <div>
    <a-page-header sub-title="用户管理" title="用户">
      <template #extra>
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
    <a-table :columns="columns"
             :data-source="data"
             :loading="loading"
             :pagination="page"
             :rowKey="(record)=>record.id"
             :scroll="tableScroll"
             bordered
             style="width: 100%" @change="tableChange">
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'status'">
          {{ getSelectLabel(status, record.status) }}
        </template>
        <template v-if="column.dataIndex === 'iconUrl'">
          <a-avatar :src="record.iconUrl" size="large" style="background-color: #66ccff">
            <template v-if="1==1">{{ record.nickname }}</template>
          </a-avatar>
        </template>
        <template v-else-if="column.dataIndex === 'operation'">
          <a-space>
            <a-tooltip title="分配角色">
              <a-button shape="circle" type="dashed" @click="openDistRoleModal(record )">
                <template #icon>
                  <AlignLeftOutlined/>
                </template>
              </a-button>
            </a-tooltip>
          </a-space>
        </template>
      </template>
    </a-table>

    <a-modal :onCancel="cancel" :onOk="ok" :title="'分配角色'" :visible="visible">
      <a-transfer
          v-model:target-keys="targetKeys"
          v-model:selected-keys="selectedKeys"
          :data-source="roleList"
          :titles="['角色', '选中角色']"
          :render="item => item.title"
      />
    </a-modal>
  </div>
</template>
<script lang="ts">
import {defineComponent, onMounted, ref} from "vue";
import {PageInfo} from "@/base/Page";
import SysUserApi from "@/api/sys/SysUserApi";
import SysDictApi from "@/api/sys/SysDictApi";
import EtRoleApi from "@/api/et/EtRoleApi";

export default defineComponent({
  name: 'SysUser',
  components: {},
  data() {
    return {
      visible: false,
      //接口url
      url: {
        get: "/sysUser/get",
      },
      //面包屑
      breadcrumbs: [
        {name: "", icon: "HomeOutlined", href: "/home"},
        {name: "用户管理", icon: "UserOutlined", href: "/sysuser/list"}
      ],
      roleList: [],
      targetKeys: [],
      selectedKeys: [],
      userId:"",
      //查询数据
      search: {},
      //表格数据
      data: [],
      //表格字段
      columns: [
        {title: '登录名', key: 'username', dataIndex: 'username'},
        {title: '用户名', key: 'nickname', dataIndex: 'nickname', ellipsis: "true"},
        {title: '状态', key: 'status', dataIndex: 'status'},
        // {title: '过期时间', key: 'expiredTime', dataIndex: 'expiredTime', width: "180px"},
        {title: '最后登录', key: 'lastLoginTime', dataIndex: 'lastLoginTime', width: "180px", sorter: true},
        {title: '头像', key: 'iconUrl', dataIndex: 'iconUrl'},
        {title: '操作', dataIndex: 'operation', fixed: 'right', width: "210px"},
      ],
    }
  },
  methods: {
    openDistRoleModal(data: any) {
      this.visible = true
      this.userId = data.id
      EtRoleApi.getAll().then(res => {
        const resultList = []
        for (let i = 0; i < res.data.data.length; i++) {
          const data = res.data.data[i]
          resultList.push({"key":data.id,"title":data.roleName,"description":data.roleDesc})
        }
        this.roleList = resultList
        console.log(this.roleList)
      })
      EtRoleApi.getRoleByUserId(data.id).then(res=>{
        this.targetKeys = res.data.data
      })
    },
    ok() {
      EtRoleApi.distRole(this.userId,this.targetKeys)
      this.visible = false
    },
    cancel() {
      this.visible = false
    },
  },
  created() {
    this.load()
  }
  ,
  setup() {
    const tablePageOption = PageInfo(SysUserApi)

    let status = ref([])
    onMounted(async () => {
      const res = await SysDictApi.getSelect('启用状态')
      status.value = res.data.data
    });

    return {
      status,
      ...tablePageOption
    }
  }
})

</script>