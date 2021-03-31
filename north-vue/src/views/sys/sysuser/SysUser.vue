<style lang="less">

</style>
<template>
  <div>
    <base-page :breadcrumbs="breadcrumbs">
      <template #content>
        <a-page-header sub-title="用户管理" title="用户">
          <template #extra>
            <a-button type="primary" @click="openImport()">导入</a-button>
            <a-button type="primary" @click="openAdd()">新增</a-button>
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
                 :scroll="{ x: 900, y: 500 }"
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
              <a-col :span="24">
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
import {Options, Vue} from 'vue-class-component';
import FormModal, {Ext, ModalField, SelectField} from "@/components/base/FormModal.vue";
import ImportModal from "@/components/base/ImportModal.vue";
import MenuModal from "@/views/sys/sysuser/MenuModal.vue";
import ChangePassword from "@/views/home/ChangePassword.vue";
import SysUserApi from "@/api/SysUserApi";
import {createVNode} from "vue";
import Qs from "qs";
import {AxiosResponse} from "axios";
import SysDictApi from "@/api/SysDictApi";

let api = new SysUserApi();
let dictApi = new SysDictApi();

@Options({
  name: 'SysUser',
  components: {
    FormModal, MenuModal, ChangePassword, ImportModal
  },
  data() {
    return {
      //表格加载状态
      loading: false,
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
        {title: '登陆名', key: 'username', dataIndex: 'username'},
        {title: '用户名', key: 'nickname', dataIndex: 'nickname', ellipsis: "true"},
        {title: '状态', key: 'status', dataIndex: 'status'},
        {title: '过期时间', key: 'expiredTime', dataIndex: 'expiredTime'},
        {title: '头像', key: 'iconUrl', dataIndex: 'iconUrl', slots: {customRender: 'iconUrl'}},
        {title: '操作', dataIndex: 'operation', slots: {customRender: 'operation'}, fixed: 'right', width: "180px"},
      ],
      //form中的字段
      formColumns: [
        new ModalField().init('登陆名', 'username', 'String'),
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
          {required: true, type: 'string', trigger: 'blur', message: "登陆名不可为空"},
          {
            validator: (rule: any, value: any, callback: any) => {
              if (!value) {
                return callback()
              }
              let originalValue = (<any>this.check).username;
              api.checkUseruame(value,originalValue).then(res => {
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
              api.checkNickname(value,originalValue).then(res => {
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
      this.check.nickname = undefined;
      this.check.username = undefined;
    },
    openEdit(id: string) {
      this.$refs.form.open({id: id})
      setTimeout(() => {
        this.check.nickname = this.$refs.form.getData().nickname;
        this.check.username = this.$refs.form.getData().username;
      }, 1000)
    },
    openMenuModal(data: any) {
      api.getRoleByUserId(data.id).then((res) => {
        data.resources = []
        res.data.data.forEach((d: any) => {
          data.resources.push(d.roleId)
        })
        this.$refs.menuModal.open(data)
      })
    },
    openMenuModal2(data: any) {
      api.getRoleByUserId(data.id).then((res) => {
        data.roleIds = []
        res.data.data.forEach((d: any) => {
          data.roleIds.push(d.roleId)
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
    openChangePassword(id: string) {
      this.$refs.cp.open(id)
    },
    resetPassword(id: string) {
      this.$modal.confirm({
        title: '重置密码',
        icon: createVNode(this.$icons["ExclamationCircleOutlined"]),
        content: '确定重置密码吗？',
        onOk: () => {
          return api.resetPassword(id).then(res => {
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
          return api.del([id]).then(res => {
            console.log(res);
            this.load()
          })
        },
      });
    },
    openImport(){
      this.$refs.importModal.open()
    },

  },
  created() {
    this.load()
  },
})

export default class SysUser extends Vue {
}

</script>