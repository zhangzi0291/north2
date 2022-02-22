<style lang="less">

</style>
<template>
  <div>
    <base-page :breadcrumbs="breadcrumbs">
      <template #content>
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
          <a-form layout="inline">
            <!--            <a-form-item label="应用名">-->
            <!--              <a-input v-model:value="search.softName" allowClear/>-->
            <!--            </a-form-item>-->

          </a-form>
        </a-row>
        <a-table :columns="columns" :data-source="data" :loading="loading" :rowKey="(record)=>record.id"
                 :scroll="{ x: 900, y: 500 }" :pagination="page" @change="tableChange"
                 bordered style="width: 100%">
          <template #logo="{ record }">
            <a-image :width="50"
                     :src="BASE_URL+'/sysFile/download?id='+record.logo"
            />
          </template>
          <template #operation="{ record }">
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
        </a-table>
      </template>

    </base-page>

    <form-modal ref="form" :columns="formColumns" :addUrl="url.add" :editUrl="url.edit" :getUrl="url.get"
                :okCallback="load" :rules="rules"
                :title="'应用'">
    </form-modal>

  </div>
</template>
<script lang="ts">
import FormModal, {Ext, ModalField} from "@/components/base/FormModal.vue";
import ImportModal from "@/components/base/ImportModal.vue";
import MenuModal from "@/views/sys/sysuser/MenuModal.vue";
import ChangePassword from "@/views/home/ChangePassword.vue";
import {createVNode, defineComponent, reactive, ref} from "vue";
import PytoolAppApi from "@/api/PytoolAppApi";


export default defineComponent({
  name: 'pytoolHomepage',
  components: {
    FormModal, MenuModal, ChangePassword, ImportModal
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
        {title: 'LOGO', key: 'logo', slots: {customRender: 'logo'}, dataIndex: 'logo'},
        {title: '描述', key: 'content', dataIndex: 'content'},
        {title: '操作', dataIndex: 'operation', slots: {customRender: 'operation'}, fixed: 'right', width: "180px"},
      ],
      //form中的字段
      formColumns: [
        new ModalField().init('应用名称', 'homeName', 'String', false),
        new ModalField().init('LOGO', 'logo', 'File', true, new Ext()),
        new ModalField().init('描述', 'content', 'Textarea'),
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
      let href = window.BASE_URL + "/sysFile/download?id=" + fileId
      window.location.href = href
    },
  },
  mounted() {
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
      PytoolAppApi.homePageList(search, page, sort).then(res => {
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
  },

})

</script>