<style lang="less">

</style>
<template>
  <div>
    <a-page-header title="应用列表">
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
        <!--            <a-form-item label="应用名">-->
        <!--              <a-input v-model:value="search.softName" allowClear/>-->
        <!--            </a-form-item>-->

      </a-form>
    </a-row>
    <a-table :columns="columns" :data-source="data" :loading="loading" :pagination="page"
             :rowKey="(record)=>record.id" :scroll="tableScroll" bordered
             style="width: 100%" @change="tableChange">
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'logo'">
          <a-image :src="BASE_URL+'/sysFile/download?id='+record.logo" :width="50"/>
        </template>
        <template v-if="column.dataIndex === 'operation'">
          <a-space>
            <a-tooltip title="编辑">
              <a-button shape="circle" type="dashed" @click="openEdit(record.homeName)">
                <template #icon>
                  <EditOutlined/>
                </template>
              </a-button>
            </a-tooltip>
            <a-tooltip title="删除">
              <a-button shape="circle" type="dashed" @click="del(record.homeName)">
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
                :title="'应用'">
    </form-modal>

  </div>
</template>
<script lang="ts">
import FormModal, {Ext, InputType, ModalField} from "@/components/base/FormModal.vue";
import {createVNode, defineComponent} from "vue";
import PytoolAppApi from "@/api/PytoolAppApi";
import {PageInfo} from "@/base/Page";


export default defineComponent({
  name: 'pytoolHomepage',
  components: {
    FormModal,
  },
  data() {
    return {
      //接口url
      url: {
        get: "/pytoolApp/getHomePageInfo",
        add: "/pytoolApp/uploadHomePageInfo",
        edit: "/pytoolApp/uploadHomePageInfo",
      },
      //面包屑
      breadcrumbs: [
        {name: "", icon: "HomeOutlined", href: "/home"},
        {name: "应用列表", icon: "UserOutlined", href: "/pytool/app"}
      ],
      //表格字段
      columns: [
        {title: '应用名称', key: 'homeName', dataIndex: 'homeName'},
        {title: 'LOGO', key: 'logo', dataIndex: 'logo'},
        {title: '描述', key: 'content', dataIndex: 'content'},
        {title: '操作', dataIndex: 'operation', fixed: 'right', width: "180px"},
      ],
      //form中的字段
      formColumns: [
        ModalField.init('应用名称', 'homeName', InputType.String, <Ext>{edit: false}),
        ModalField.init('LOGO', 'logo', InputType.File),
        ModalField.init('描述', 'content', InputType.Textarea),
      ],
      //fomr校验规则
      rules: {
        homeName: [
          {required: true, type: 'string', trigger: 'blur', message: "应用名称不可为空"},
        ],
      },
    }
  },
  methods: {
    openAdd() {
      const form: any = this.$refs.form
      form.open()
    },
    openEdit(homeName: string) {
      const form: any = this.$refs.form
      form.open({id: homeName})
    },
    del(id: string) {
      this.$modal.confirm({
        title: '删除',
        icon: createVNode(this.$icons["ExclamationCircleOutlined"]),
        content: '确定要删除吗？',
        onOk: () => {
          return PytoolAppApi.homePageDel([id]).then(res => {
            console.log(res);
            this.load()
          })
        },
      });
    },
    download(fileId: string) {
      window.location.href = window.BASE_URL + "/sysFile/download?id=" + fileId
    },
  },
  mounted() {
    this.load()
  },
  setup() {
    const tablePageOption = PageInfo(PytoolAppApi)

    return {
      ...tablePageOption
    }
  },

})

</script>