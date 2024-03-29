<style lang="less">

</style>
<template>
  <div>
    <a-page-header sub-title="用户管理" title="用户">
      <template #extra>
        <!--            <template v-if="hasPermissionOr('用户列表导入')">-->
        <a-button type="primary" @click="openImport()">导入</a-button>
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
            <a-tooltip :title="record.status == 0?'启用':'停用'">
              <a-button shape="circle" type="dashed" @click="changeUserStatus(record.id, record)">
                <template #icon>
                  <CheckOutlined v-if="record.status==0"/>
                  <CloseOutlined v-else/>
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
      </template>
    </a-table>


    <form-modal ref="form" :addUrl="url.add" :columns="formColumns" :editUrl="url.edit" :getUrl="url.get"
                :okCallback="load" :rules="rules"
                :title="'用户'">
      <template #roleIds="{data}">
        <a-button type="primary" @click="openMenuModal(data)">分配角色</a-button>
      </template>
    </form-modal>
    <menu-modal ref="menuModal" :okCallback="roleIdsOk"></menu-modal>
    <menu-modal ref="menuModal2" :okCallback="menuModalOkCallback"></menu-modal>

    <import-modal ref="importModal" :title="'用户导入'" :url="url.import"></import-modal>

    <change-password ref="cp"></change-password>
  </div>
</template>
<script lang="ts">
import FormModal, {Ext, InputType, ModalField, SelectField} from "@/components/base/FormModal.vue";
import ImportModal from "@/components/base/ImportModal.vue";
import MenuModal from "@/views/sys/sysuser/MenuModal.vue";
import ChangePassword from "@/views/home/ChangePassword.vue";
import {createVNode, defineComponent, onMounted, ref} from "vue";
import {PageInfo} from "@/base/Page";
import SysUserApi from "@/api/sys/SysUserApi";
import SysDictApi from "@/api/sys/SysDictApi";

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
        {title: '最后登录', key: 'lastLoginTime', dataIndex: 'lastLoginTime', width: "180px", sorter: true},
        {title: '头像', key: 'iconUrl', dataIndex: 'iconUrl'},
        {title: '操作', dataIndex: 'operation', fixed: 'right', width: "210px"},
      ],
      //form中的字段
      formColumns: [
        ModalField.init('登录名', 'username', InputType.String),
        ModalField.init('用户名', 'nickname', InputType.String),
        ModalField.init('密码', 'password', InputType.Password, <Ext>{edit: false}),
        ModalField.init('手机号', 'phone', InputType.String),
        ModalField.init('Email', 'email', InputType.String),
        ModalField.init('状态', 'status', InputType.Select, <Ext>{
          selectParameter: {
            array: [SelectField.init("启用", 1), SelectField.init("禁用", 0)],
            dictName: "启用状态"
          }
        }),
        ModalField.init('过期时间', 'expiredTime', InputType.DateTime),
        ModalField.init('ICON', 'iconUrl', InputType.Image),
        ModalField.init('描述', 'describe', InputType.Textarea),
        ModalField.init('角色', 'roleIds', InputType.Slot),
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
              SysUserApi.checkUseruame(value, originalValue).then(res => {
                if (res.data.code == 40001) {
                  callback(res.data.msg)
                } else if (res.data.code == 200) {
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
              SysUserApi.checkNickname(value, originalValue).then(res => {
                if (res.data.code == 40001) {
                  callback(res.data.msg)
                } else if (res.data.code == 200) {
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
      SysUserApi.getRoleByUserId(data.id).then((res) => {
        data.resources = []
        res.data.data.forEach((d: any) => {
          data.resources.push(d.roleId)
        })
        const menuModal: any = this.$refs.menuModal
        menuModal.open(data)
      })
    },
    openMenuModal2(data: any) {
      SysUserApi.getRoleByUserId(data.id).then((res) => {
        data.roleIds = []
        res.data.data.forEach((d: any) => {
          data.roleIds.push(d.roleId)
        })
        const menuModal2: any = this.$refs.menuModal2
        menuModal2.open(data)
      })
    },
    roleIdsOk() {
      const form: any = this.$refs.form
      form.validate()
    },
    menuModalOkCallback(data: any) {
      SysUserApi.editWithResource(data).then(() => {
        this.$message.success("操作成功")
      });
    },
    openChangePassword(id: string) {
      const cp: any = this.$refs.cp
      cp.open(id)
    },
    resetPassword(id: string) {
      this.$modal.confirm({
        title: '重置密码',
        icon: createVNode(this.$icons["ExclamationCircleOutlined"]),
        content: '确定重置密码吗？',
        onOk: () => {
          return SysUserApi.resetPassword(id).then(() => {
            this.$message.success("操作成功")
          })
        },
      });
    },
    changeUserStatus(id: string, record: any) {
      const text = record.status == 0 ? '启用' : '停用'
      this.$modal.confirm({
        title: '修改用户状态',
        icon: createVNode(this.$icons["ExclamationCircleOutlined"]),
        content: '确定' + text + '用户吗？',
        onOk: () => {
          return SysUserApi.changeUserStatus(id).then(() => {
            this.$message.success("操作成功")
            const status = record.status == 0 ? 1 : 0;
            record.status = status
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
    openImport() {
      const importModal: any = this.$refs.importModal
      importModal.open()
    },

  },
  created() {
    this.load()
  },
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