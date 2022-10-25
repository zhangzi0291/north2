<style lang="less">

</style>
<template>
  <div>
    <a-page-header title="ET角色">
      <template #extra>
        <!--            <template v-if="hasPermissionOr('用户列表导入')">-->
        <a-button type="primary" @click="openAdd()">新增</a-button>
        <!--            </template>-->
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
        <template v-if="column.dataIndex === 'imgId'">
          <a-image :src="BASE_URL+'sysFile/download?id='+record.imgId" :width="50"/>
        </template>
        <template v-else-if="column.dataIndex === 'operation'">
          <a-space>
            <a-tooltip title="编辑">
              <a-button shape="circle" type="dashed" @click="openEdit(record.id)">
                <template #icon>
                  <EditOutlined/>
                </template>
              </a-button>
            </a-tooltip>
            <a-tooltip title="分配应用">
              <a-button shape="circle" type="dashed" @click="openDistAppModal(record )">
                <template #icon>
                  <AlignLeftOutlined/>
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
                :okCallback="load" :rules="rules"
                :title="'用户'">
    </form-modal>
    <a-modal :onCancel="cancel" :onOk="ok" :title="'分配应用'" :visible="visible">
      <a-transfer
          v-model:target-keys="targetKeys"
          v-model:selected-keys="selectedKeys"
          :data-source="appList"
          :titles="['角色', '选中角色']"
          :render="item => item.title"
      />
    </a-modal>
  </div>
</template>
<script lang="ts">
import FormModal, {InputType, ModalField} from "@/components/base/FormModal.vue";
import {createVNode, defineComponent} from "vue";
import {PageInfo} from "@/base/Page";
import EtRoleApi from "@/api/et/EtRoleApi";
import EtAppInfoApi from "@/api/et/EtAppInfoApi";

export default defineComponent({
  name: 'EtRole',
  components: {
    FormModal,
  },
  data() {
    return {
      visible:false,
      //接口url
      url: {
        get: "/etRole/get",
        add: "/etRole/add",
        edit: "/etRole/edit",
      },
      //面包屑
      breadcrumbs: [
        {name: "", icon: "HomeOutlined", href: "/home"},
        {name: "用户管理", icon: "UserOutlined", href: "/sysuser/list"}
      ],
      appList: [],
      targetKeys: [],
      selectedKeys: [],
      //查询数据
      search: {},
      //表格数据
      data: [],
      //表格字段
      columns: [
        {title: 'ET角色名称', key: 'roleName', dataIndex: 'roleName'},
        {title: '描述', key: 'roleDesc', dataIndex: 'roleDesc'},
        {title: '操作', dataIndex: 'operation', fixed: 'right', width: "210px"},
      ],
      //form中的字段
      formColumns: [
        ModalField.init('ET角色名称', 'roleName', InputType.String),
        ModalField.init('描述', 'roleDesc', InputType.Textarea),
      ],
      //fomr校验规则
      rules: {},
      check: {
        nickname: "",
        username: "",
      }
    }
  },
  methods: {
    openAdd(parentId: string) {
      const form: any = this.$refs.form
      form.open({parentId: parentId})
      const check: any = this.check
      check.nickname = undefined;
      check.username = undefined;
    },
    openEdit(id: string) {
      const form: any = this.$refs.form
      form.open({id: id})
      setTimeout(() => {
        this.check.nickname = form.getData().nickname;
        this.check.username = form.getData().username;
      }, 1000)
    },
    openDistAppModal(data: any) {
      this.visible = true
      this.roleId = data.id
      EtAppInfoApi.getAll().then(res => {
          const resultList = []
          for (let i = 0; i < res.data.data.length; i++) {
            const data = res.data.data[i]
            resultList.push({"key":data.id,"title":data.appName})
          }
          this.appList = resultList
          console.log(this.appList)
        })
      EtAppInfoApi.getAppByRoleId(data.id).then(res=>{
        this.targetKeys = res.data.data
        console.log(this.targetKeys)
      })
    },
    del(id: string) {
      this.$modal.confirm({
        title: '删除',
        icon: createVNode(this.$icons["ExclamationCircleOutlined"]),
        content: '确定要删除吗？',
        onOk: () => {
          return EtRoleApi.del([id]).then(res => {
            console.log(res);
            this.load()
          })
        },
      });
    }, ok() {
      EtAppInfoApi.distApp(this.roleId,this.targetKeys)
      this.visible = false
    }, cancel() {
      this.visible = false
    },
  },
  created() {
    this.load()
  },
  setup() {
    const tablePageOption = PageInfo(EtRoleApi)


    return {
      ...tablePageOption
    }
  }
})

</script>