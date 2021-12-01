<style lang="less">

</style>
<template>
  <div>
    <base-page :breadcrumbs="breadcrumbs">
      <template #content>
        <a-page-header sub-title="用户管理" title="用户">
          <template #extra>
            <template v-if="hasPermissionOr('用户列表导入')">
              <a-button type="primary" @click="openImport()">导入</a-button>
              <a-button type="primary" @click="openAdd()">新增</a-button>
            </template>
            <a-button type="primary" @click="load({current:1})">查询</a-button>
          </template>
        </a-page-header>
        <a-row>
          <b>检索条件</b>
        </a-row>
        <a-row>
          <a-form layout="inline">
            <a-form-item label="用户名">
              <a-input v-model:value="search.nickname" allowClear/>
            </a-form-item>

          </a-form>
        </a-row>
        <a-table :columns="columns" :data-source="data" :loading="loading" :rowKey="(record)=>record.id"
                 :scroll="{ x: 900, y: 500 }"  :pagination="page" @change="tableChange"
                 bordered style="width: 100%">
          <template #expandedRowRender="{ record }">
            <a-row>
              <a-col :span="12">
                手机号：{{ record.phone }}
              </a-col>
              <a-col :span="12">
                邮箱：{{ record.email }}
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                过期时间：{{ record.expiredTime }}
              </a-col>
              <a-col :span="12">
                描述：{{ record.describe }}
              </a-col>
            </a-row>
          </template>
          <template #iconUrl="{ record }">
            <a-avatar :src="record.iconUrl" size="large" style="background-color: #66ccff">
              <template v-if="1==1">{{ record.nickname }}</template>
            </a-avatar>
          </template>
          <template #operation="{ record }">
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
              <a-tooltip title="重置密码">
                  <a-button shape="circle" type="dashed" @click="resetPassword(record.id )">
                    <template #icon>
                      <LockOutlined/>
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
                :title="'用户'">
      <template #roleIds="{data}">
        <a-button type="primary" @click="openMenuModal(data)">分配角色</a-button>
      </template>
    </form-modal>
    <menu-modal ref="menuModal"></menu-modal>
    <menu-modal ref="menuModal2" :okCallback="menuModalOkCallback"></menu-modal>

    <import-modal ref="importModal" :title="'用户导入'" :url="url.import"></import-modal>

    <change-password ref="cp"></change-password>
  </div>
</template>
<script lang="ts">
import FormModal, {Ext, ModalField, SelectField} from "@/components/base/FormModal.vue";
import ImportModal from "@/components/base/ImportModal.vue";
import MenuModal from "@/views/sys/sysuser/MenuModal.vue";
import ChangePassword from "@/views/home/ChangePassword.vue";
import SysUserApi from "@/api/SysUserApi";
import {createVNode, defineComponent, reactive, ref,toRefs} from "vue";
import Qs from "qs";
import {AxiosResponse} from "axios";

let api = new SysUserApi();

export default defineComponent({
  name: 'SysUser',
  components: {
    FormModal, MenuModal, ChangePassword, ImportModal
  },
  data() {
    return {
      //接口url
      url: {
        get: "/sysUser/get",
        add: "/sysUser/addWithRole",
        edit: "/sysUser/editWithRole",
        import: "/sysUser/import",
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
        {title: '登录名', key: 'username', dataIndex: 'username'},
        {title: '用户名', key: 'nickname', dataIndex: 'nickname', ellipsis: "true"},
        {title: '状态', key: 'status', dataIndex: 'status'},
        // {title: '过期时间', key: 'expiredTime', dataIndex: 'expiredTime', width: "180px"},
        {title: '最后登录', key: 'lastLoginTime', dataIndex: 'lastLoginTime', width: "180px",sorter: true},
        {title: '头像', key: 'iconUrl', dataIndex: 'iconUrl', slots: {customRender: 'iconUrl'}},
        {title: '操作', dataIndex: 'operation', slots: {customRender: 'operation'}, fixed: 'right', width: "180px"},
      ],
      //form中的字段
      formColumns: [
        new ModalField().init('登录名', 'username', 'String'),
        new ModalField().init('用户名', 'nickname', 'String'),
        new ModalField().init('密码', 'password', 'Password', false),
        new ModalField().init('手机号', 'phone', 'String'),
        new ModalField().init('Email', 'email', 'String'),
        new ModalField().initSelect('状态', 'status', new Ext(), [new SelectField("启用", 1), new SelectField("禁用", 0)],"启用状态"),
        new ModalField().init('过期时间', 'expiredTime', 'DateTime'),
        new ModalField().init('ICON', 'iconUrl', 'Image', true, new Ext()),
        new ModalField().init('描述', 'describe', 'String'),
        new ModalField().init('角色', 'roleIds', 'Slot', false),
      ],
      //fomr校验规则
      rules: {
        username: [
          {required: true, type: 'string', trigger: 'blur', message: "登录名不可为空"},
          {
            validator: (rule: any, value: any, callback: any) => {
              if (!value) {
                return callback()
              }
              let originalValue = (<any>this.check).username;
              SysUserApi.checkUseruame(value,originalValue).then(res => {
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
        nickname: [
          {required: true, type: 'string', trigger: 'blur', message: "用户名不可为空"},
          {
            validator: (rule: any, value: any, callback: any) => {
              if (!value) {
                return callback()
              }
              let originalValue = (<any>this.check).nickname;
              SysUserApi.checkNickname(value,originalValue).then(res => {
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
        password: [
          {required: true, type: 'string', trigger: 'blur', message: "密码不可为空"},
        ],
        roleIds: [
          {required: true, type: 'array', trigger: 'change', message: "角色不可为空"},
        ],

      },
      check:{
        nickname: "",
        username: "",
      }
    }
  },
  methods: {

    openAdd(parentId: string) {
      console.log(this.data)
      console.log(this.loading)
      const form:any = this.$refs.form
      form.open({parentId: parentId})
      const check:any = this.check
      check.nickname = undefined;
      check.username = undefined;
    },
    openEdit(id: string) {
      const form:any = this.$refs.form
      form.open({id: id})
      setTimeout(() => {
        this.check.nickname = form.getData().nickname;
        this.check.username = form.getData().username;
      }, 1000)
    },
    openMenuModal(data: any) {
      SysUserApi.getRoleByUserId(data.id).then((res) => {
        data.resources = []
        res.data.data.forEach((d: any) => {
          data.resources.push(d.roleId)
        })
        const menuModal:any = this.$refs.menuModal
        menuModal.open(data)
      })
    },
    openMenuModal2(data: any) {
      SysUserApi.getRoleByUserId(data.id).then((res) => {
        data.roleIds = []
        res.data.data.forEach((d: any) => {
          data.roleIds.push(d.roleId)
        })
        const menuModal2:any = this.$refs.menuModal2
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
    openChangePassword(id: string) {
      const cp:any = this.$refs.cp
      cp.open(id)
    },
    resetPassword(id: string) {
      this.$modal.confirm({
        title: '重置密码',
        icon: createVNode(this.$icons["ExclamationCircleOutlined"]),
        content: '确定重置密码吗？',
        onOk: () => {
          return SysUserApi.resetPassword(id).then(res => {
            this.$message.success("操作成功")
          })
        },
      });
    },
    del(id: string) {
      this.$modal.confirm({
        title: '删除',
        icon: createVNode(this.$icons["ExclamationCircleOutlined"]),
        content: '确定要删除吗？',
        onOk: () => {
          return SysUserApi.del([id]).then(res => {
            console.log(res);
            this.load()
          })
        },
      });
    },
    openImport(){
      const importModal:any = this.$refs.importModal
      importModal.open()
    },

  },
  created() {
    this.load()
  },
  setup(){
    //表格加载状态
    let loading = ref(false)
    //分页
    let page = reactive({ current:1,total:0 })
    //排序
    let sort = reactive({ field:null,order:null})
    //查询数据
    let search = reactive({ })
    //表格数据
    let data = ref([])

    const load = function (param?:any) {
      loading.value = true
      if(!!param && !!param.current ){
        page.current = param.current
      }
      SysUserApi.list(search,page,sort).then(res => {
        data.value.length=0
        data.value = data.value.concat(res.data.data.records)
        page.current = res.data.data.current
        page.total = res.data.data.total
        loading.value = false
      })
    }
    const tableChange = function (pageParam:any, filters:any, sorter:any){
      page.current = pageParam.current
      page.total = pageParam.total
      sort.field = sorter.field
      sort.order = sorter.order
      load()
    }
    const tablePageOption = {loading,data,page,search,load,tableChange}

    return {
      ...tablePageOption
    }
  }
})

</script>