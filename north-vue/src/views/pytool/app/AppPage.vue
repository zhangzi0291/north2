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
            <a-form-item label="应用名">
              <a-input v-model:value="search.softName" allowClear/>
            </a-form-item>

          </a-form>
        </a-row>
        <a-table :columns="columns" :data-source="data" :loading="loading" :rowKey="(record)=>record.id"
                 :scroll="{ x: 900, y: 500 }" :pagination="page" @change="tableChange"
                 bordered style="width: 100%">
          <template #operation="{ record }">
            <a-space>
              <a-tooltip title="历史">
                <a-button shape="circle" type="dashed" @click="openHistory(record.softName)">
                  <template #icon>
                    <HistoryOutlined/>
                  </template>
                </a-button>
              </a-tooltip>
              <a-tooltip title="编辑">
                <a-button shape="circle" type="dashed" @click="openEdit(record.id)">
                  <template #icon>
                    <EditOutlined/>
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
              <a-tooltip title="下载">
                <a-button shape="circle" type="dashed" @click="download(record.fileId)" v-if="record.fileId">
                  <template #icon>
                    <DownloadOutlined/>
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
                :title="'应用'">
      <template #roleIds="{data}">
        <a-button type="primary" @click="openMenuModal(data)">分配角色</a-button>
      </template>
    </form-modal>
    <!--    <menu-modal ref="menuModal"></menu-modal>-->
    <!--    <menu-modal ref="menuModal2" :okCallback="menuModalOkCallback"></menu-modal>-->

    <import-modal ref="importModal" :title="'用户导入'" :url="url.import"></import-modal>

    <change-password ref="cp"></change-password>

    <a-modal :onCancel="cancel" :onOk="ok" :title="'历史版本'" :visible="visibleHis" width="80%">
      <a-table :columns="hisColumns" :data-source="hisData" :loading="hisLoading" :rowKey="(record)=>'his'+record.id"
               :pagination="hisPage" @change="hisTableChange"
               bordered style="width: 100%">
        <template #operation="{ record }">
          <a-tooltip title="下载">
            <a-button shape="circle" type="dashed" @click="download(record.fileId)" v-if="record.fileId">
              <template #icon>
                <DownloadOutlined/>
              </template>
            </a-button>
          </a-tooltip>
        </template>
      </a-table>
    </a-modal>

  </div>
</template>
<script lang="ts">
import {Options, Vue} from 'vue-class-component';
import FormModal, {Ext, ModalField} from "@/components/base/FormModal.vue";
import ImportModal from "@/components/base/ImportModal.vue";
import MenuModal from "@/views/sys/sysuser/MenuModal.vue";
import ChangePassword from "@/views/home/ChangePassword.vue";
import {createVNode} from "vue";
import PytoolAppApi from "@/api/PytoolAppApi";


@Options({
  name: 'pytoolApp',
  components: {
    FormModal, MenuModal, ChangePassword, ImportModal
  },
  data() {
    return {
      hisColumns: [
        {title: '应用名称', key: 'softName', dataIndex: 'softName', width: "100px"},
        {title: '应用版本', key: 'softVersion', dataIndex: 'softVersion', width: "100px"},
        {title: '版本更新时间', key: 'versionUpdateTime', dataIndex: 'versionUpdateTime', width: "100px"},
        {title: '操作', dataIndex: 'operation', slots: {customRender: 'operation'}, fixed: 'right', width: "100px"},
      ],
      hisData: [],
      hisLoading: false,
      visibleHis: false,
      //表格加载状态
      loading: false,
      //接口url
      url: {
        get: "/pytoolApp/get",
        add: "/pytoolApp/addWithFile",
        edit: "/pytoolApp/editWithFile",
      },
      //面包屑
      breadcrumbs: [
        {name: "", icon: "HomeOutlined", href: "/home"},
        {name: "应用列表", icon: "UserOutlined", href: "/pytool/app"}
      ],
      //查询数据
      search: {},
      //表格数据
      data: [],
      //表格字段
      columns: [
        {title: '应用名称', key: 'softName', dataIndex: 'softName'},
        {title: '应用版本', key: 'softVersion', dataIndex: 'softVersion'},
        {title: '版本更新时间', key: 'versionUpdateTime', dataIndex: 'versionUpdateTime'},
        {title: '操作', dataIndex: 'operation', slots: {customRender: 'operation'}, fixed: 'right', width: "180px"},
      ],
      //form中的字段
      formColumns: [
        new ModalField().init('应用名称', 'softName', 'String', false),
        new ModalField().init('应用版本', 'softVersion', 'String'),
        new ModalField().init('版本文件', 'file', 'File', true, new Ext()),
      ],
      //fomr校验规则
      rules: {
        softName: [
          {required: true, type: 'string', trigger: 'blur', message: "应用名称不可为空"},
          {
            validator: (rule: any, value: any, callback: any) => {
              if (!value) {
                return callback()
              }
              let originalValue = (<any>this.check).username;
              PytoolAppApi.checkSoftName(value, originalValue).then(res => {
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
        softVersion: [
          {required: true, type: 'string', trigger: 'blur', message: "应用版本不可为空"},
        ],

      },
      check: {
        nickname: "",
        username: "",
      }
    }
  },
  methods: {
    load(data: any) {
      this.loading = true
      if (!!data && !!data.current) {
        this.page.current = data.current
      }
      PytoolAppApi.list(this.search, this.page, this.sort).then(res => {
        this.data = res.data.data.records
        this.page = {
          current: res.data.data.current,
          total: res.data.data.total
        }
        this.loading = false
      })
    },
    tableChange(page: any, filters: any, sorter: any) {
      this.page = page
      this.sort = {
        field: sorter.field,
        order: sorter.order
      }
      this.load()
    },
    loadHis(softName: string) {
      this.hisLoading = true
      PytoolAppApi.gethistoryList(softName, this.hisPage).then(res => {
        this.hisData = res.data.data.records
        this.hisPage = {
          current: res.data.data.current,
          total: res.data.data.total
        }
        this.hisLoading = false
      })
    },
    hisTableChange(page: any) {
      this.hisPage = page
      this.loadHis()
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
    del(id: string) {
      this.$modal.confirm({
        title: '删除',
        icon: createVNode(this.$icons["ExclamationCircleOutlined"]),
        content: '确定要删除吗？',
        onOk: () => {
          return PytoolAppApi.del([id]).then(res => {
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
    openHistory(softName: string) {
      this.visibleHis = true
      this.loadHis(softName)
    },
    ok() {
      this.visibleHis = false

    },
    cancel() {
      this.visibleHis = false

    }
  },
  created() {
    this.load()
  },

})

export default class SysUser extends Vue {
}

</script>