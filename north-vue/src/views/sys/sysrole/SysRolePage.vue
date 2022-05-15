<style lang="less">

</style>
<template>
  <div>
    <base-page :breadcrumbs="breadcrumbs">
      <template #content>
        <a-page-header sub-title="角色配置" title="角色">
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
            <a-form-item label="角色名">
              <a-input v-model:value="search.roleName" allowClear/>
            </a-form-item>
          </a-form>
        </a-row>
        <a-table :columns="columns" :data-source="data" :loading="loading" :rowKey="(record)=>record.id"
                 :scroll="{ x: 900, y: 500 }" :pagination="page" @change="tableChange"
                 bordered>
          <template #operation="{ record }">
            <a-space>
              <a-tooltip title="编辑">
                <a-button shape="circle" type="dashed" @click="openEdit(record.id)">
                  <template #icon>
                    <EditOutlined/>
                  </template>
                </a-button>
              </a-tooltip>
              <a-tooltip title="分配资源">
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
        </a-table>
      </template>

    </base-page>
    <form-modal ref="form" :addUrl="url.add" :columns="formColumns" :editUrl="url.edit" :getUrl="url.get"
                :okCallback="load" :rules="rules"
                :title="'角色'">
      <template #resources="{data}">
        <a-button type="primary" @click="openMenuModal(data)">分配资源</a-button>
      </template>
    </form-modal>

    <menu-modal ref="menuModal" :title="'资源分配'" :okCallback="menuModalOkCallback"></menu-modal>
    <menu-modal ref="menuModal2" :title="'资源分配'" :okCallback="menuModalOkCallback"></menu-modal>
  </div>
</template>
<script lang="ts">
import SysRoleApi from '@/api/SysRoleApi'
import FormModal, {InputType, ModalField} from "@/components/base/FormModal.vue";
import MenuModal from "@/views/sys/sysresource/MenuModal.vue";
import {createVNode, defineComponent, reactive, ref} from "vue";
import Qs from "qs";
import {AxiosResponse} from "axios";

let api = new SysRoleApi();

export default defineComponent({
  name: 'SysRole',
  components: {
    FormModal, MenuModal
  },
  data() {
    return {
      //接口url
      url: {
        get: "/sysRole/get",
        add: "/sysRole/addWithResource",
        edit: "/sysRole/editWithResource",
      },
      //面包屑
      breadcrumbs: [
        {name: "", icon: "HomeOutlined", href: "/home"},
        {name: "角色管理", icon: "KeyOutlined", href: "/sysrole/list"}
      ],
      //表格字段
      columns: [
        {title: '角色名称', key: 'roleName', dataIndex: 'roleName',},
        {title: '角色描述', key: 'describe', dataIndex: 'describe',},
        {title: '操作', dataIndex: 'operation', slots: {customRender: 'operation'}, fixed: 'right', width: "150px",},
      ],
      //form中的字段
      formColumns: [
        ModalField.init('角色名称', 'roleName', InputType.String),
        ModalField.init('角色描述', 'describe', InputType.String),
        ModalField.init('下属资源', 'resources', InputType.Slot),
      ],
      //fomr校验规则
      rules: {
        roleName: [
          {required: true, type: 'string', trigger: 'blur', message: "角色名称不可为空"},
          {
            validator: (rule: any, value: any, callback: any) => {
              if (!value) {
                return callback()
              }
              let originalValue = (<any>this.check).roleName;
              SysRoleApi.checkRoleName(value, originalValue).then(res => {
                if (res.data.code == '40001') {
                  callback(res.data.msg)
                } else if (res.data.code == '200') {
                  callback()
                } else {
                  callback("错误")
                }
              })
            }, trigger: 'blur',
          },
        ],
        resources: [
          {required: true, type: 'array', trigger: 'change', message: "资源不可为空"},
        ]
      },
      check: {
        roleName: "",
      }
    }
  },
  methods: {

    openAdd(parentId: string) {
      const form: any = this.$refs.form
      const check: any = this.check
      form.open({parentId: parentId})
      check.roleName = undefined;
    },
    openEdit(id: string) {
      const form: any = this.$refs.form
      const check: any = this.check
      form.open({id: id})
      setTimeout(() => {
        check.roleName = form.getData().roleName;
      }, 1000)
    },
    openMenuModal(data: any) {
      SysRoleApi.getResourceByRoleId(data.id).then((res) => {
        data.resources = []
        res.data.data.forEach((d: any) => {
          data.resources.push(d.resourceId)
        })
        const menuModal: any = this.$refs.menuModal
        menuModal.open(data)
      })
    },
    openMenuModal2(data: any) {
      SysRoleApi.getResourceByRoleId(data.id).then((res) => {
        data.resources = []
        res.data.data.forEach((d: any) => {
          data.resources.push(d.resourceId)
        })
        const menuModal2: any = this.$refs.menuModal2
        menuModal2.open(data)
      })
    },
    menuModalOkCallback(data: any) {
      console.log(data)
      this.$axios({
        method: "post",
        url: this.url.edit,
        data: Qs.stringify((data), {indices: false})
      }).then((res: AxiosResponse) => {
        this.$message.success("操作成功")
      });
    },
    del(id: string) {
      this.$modal.confirm({
        title: '删除',
        icon: createVNode(this.$icons["ExclamationCircleOutlined"]),
        content: '确定要删除吗？',
        onOk: () => {
          return SysRoleApi.del([id]).then(res => {
            console.log(res);
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
      SysRoleApi.list(search, page, sort).then(res => {
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