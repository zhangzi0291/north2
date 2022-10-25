<style lang="less">

</style>
<template>
  <div>
    <a-page-header title="ET应用">
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
            <a-tooltip title="分配角色">
              <a-button shape="circle" type="dashed" @click="openMenuModal2(record )">
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
      <template #roleIds="{data}">
        <a-button type="primary" @click="openMenuModal(data)">分配角色</a-button>
      </template>
    </form-modal>

  </div>
</template>
<script lang="ts">
import FormModal, {InputType, ModalField} from "@/components/base/FormModal.vue";
import {createVNode, defineComponent} from "vue";
import {PageInfo} from "@/base/Page";
import EtAppInfoApi from "@/api/et/EtAppInfoApi";

export default defineComponent({
  name: 'EtApp',
  components: {
    FormModal,
  },
  data() {
    return {
      //接口url
      url: {
        get: "/etAppInfo/get",
        add: "/etAppInfo/addApp",
        edit: "/etAppInfo/editApp",
      },
      //面包屑
      breadcrumbs: [
        {name: "", icon: "HomeOutlined", href: "/home"},
        {name: "用户管理", icon: "UserOutlined", href: "/sysuser/list"}
      ],
      //查询数据
      search: {},
      //表格数据
      data: [],
      //表格字段
      columns: [
        {title: 'APP名称', key: 'appName', dataIndex: 'appName'},
        {title: '图片', key: 'imgId', dataIndex: 'imgId'},
        {title: '路径', key: 'appUrl', dataIndex: 'appUrl'},
        {title: '操作', dataIndex: 'operation', fixed: 'right', width: "210px"},
      ],
      //form中的字段
      formColumns: [
        ModalField.init('APP名称', 'appName', InputType.String),
        ModalField.init('图片', 'imgId', InputType.Image),
        ModalField.init('路径', 'appUrl', InputType.String),
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
    openMenuModal(data: any) {

    },
    menuModalOkCallback(data: any) {

    },
    del(id: string) {
      this.$modal.confirm({
        title: '删除',
        icon: createVNode(this.$icons["ExclamationCircleOutlined"]),
        content: '确定要删除吗？',
        onOk: () => {
          return EtAppInfoApi.del([id]).then(res => {
            console.log(res);
            this.load()
          })
        },
      });
    },
  },
  created() {
    this.load()
  },
  setup() {
    const tablePageOption = PageInfo(EtAppInfoApi)

    return {
      ...tablePageOption
    }
  }
})

</script>