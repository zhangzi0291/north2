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
                 :scroll="{ x: 900, y: 500 }"
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
import {Options, Vue} from 'vue-class-component';
import FormModal, {ModalField} from "@/components/base/FormModal.vue";
import MenuModal from "@/views/sys/sysresource/MenuModal.vue";
import {createVNode} from "vue";
import Qs from "qs";
import {AxiosResponse} from "axios";

let api = new SysRoleApi();

@Options({
  name: 'SysRole',
  components: {
    FormModal, MenuModal
  },
  data() {
    return {
      //表格加载状态
      loading: false,
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
      //查询数据
      search: {},
      //表格数据
      data: [],
      //表格字段
      columns: [
        {title: '角色名称', key: 'roleName', dataIndex: 'roleName',},
        {title: '角色描述', key: 'describe', dataIndex: 'describe',},
        {title: '操作', dataIndex: 'operation', slots: {customRender: 'operation'}, fixed: 'right', width: "150px",},
      ],
      //form中的字段
      formColumns: [
        new ModalField().init('角色名称', 'roleName', 'String'),
        new ModalField().init('角色描述', 'describe', 'String'),
        new ModalField().init('下属资源', 'resources', 'Slot', false),
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
              api.checkRoleName(value, originalValue).then(res => {
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
      check:{
        roleName: "",
      }
    }
  },
  methods: {
    load(data:any) {
      this.loading = true
      if(!!data && !!data.current ){
        this.page.current = data.current
      }
      api.list(this.search,this.page,this.sort).then(res => {
        this.data = res.data.data.records
        this.page = {
          current: res.data.data.current,
          total: res.data.data.total
        }
        this.loading = false
      })
    },
    openAdd(parentId: string) {
      this.$refs.form.open({parentId: parentId})
      this.check.roleName = undefined;
    },
    openEdit(id: string) {
      this.$refs.form.open({id: id})
      setTimeout(() => {
        this.check.roleName = this.$refs.form.getData().roleName;
      }, 1000)
    },
    openMenuModal(data: any) {
      api.getResourceByRoleId(data.id).then((res) => {
        data.resources = []
        res.data.data.forEach((d: any) => {
          data.resources.push(d.resourceId)
        })
        this.$refs.menuModal.open(data)
      })
    },
    openMenuModal2(data: any) {
      api.getResourceByRoleId(data.id).then((res) => {
        data.resources = []
        res.data.data.forEach((d: any) => {
          data.resources.push(d.resourceId)
        })
        this.$refs.menuModal2.open(data)
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
          return api.del([id]).then(res => {
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
})

export default class SysRole extends Vue {
}

</script>