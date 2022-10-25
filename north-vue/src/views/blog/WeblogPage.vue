<style lang="less">

</style>
<template>
  <div>
    <a-page-header sub-title="" title="博客后台">
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
        <a-form-item label="博客标题">
          <a-input v-model:value="search.weblogTitle" allowClear/>
        </a-form-item>
      </a-form>
    </a-row>
    <a-table :columns="columns" :data-source="data" :loading="loading" :pagination="page"
             :rowKey="(record)=>record.id" :scroll="tableScroll" bordered
             @change="tableChange">
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex == 'type'">
          <span>
            {{ getSelectLabel(weblogType, record.type) }}
          </span>
        </template>
        <template v-if="column.dataIndex == 'status'">
          <span>
            {{ getSelectLabel(weblogStatus, record.status) }}
          </span>
        </template>
        <template v-if="column.dataIndex == 'weblogTitleImage'">
          <a-image :src="BASE_URL+'/sysFile/download?id='+record.weblogTitleImage" :width="50"/>
        </template>
        <template v-if="column.dataIndex === 'operation'">
          <a-space>
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
          </a-space>
        </template>
      </template>
    </a-table>
    <form-modal ref="form" :addUrl="url.add" :columns="formColumns" :editUrl="url.edit" :getUrl="url.get"
                :okCallback="load" :rules="rules" :title="'字典'">
      <template #resources="{data}">
        <a-button type="primary" @click="openMenuModal(data)">分配资源</a-button>
      </template>
    </form-modal>

  </div>
</template>
<script lang="ts">
import FormModal, {Ext, InputType, ModalField} from "@/components/base/FormModal.vue";
import {createVNode, defineComponent, onMounted, ref} from "vue";
import WeblogApi from "@/api/WeblogApi";
import {PageInfo} from "@/base/Page";
import SysDictApi from "@/api/sys/SysDictApi";


export default defineComponent({
  name: 'Weblog',
  components: {
    FormModal,
  },
  data() {
    return {
      //接口url
      url: {
        get: "/weblog/get",
        add: "/weblog/addWeblog",
        edit: "/weblog/editWeblog",
      },
      //面包屑
      breadcrumbs: [
        {name: "", icon: "HomeOutlined", href: "/home"},
        {name: "博客后台", icon: "KeyOutlined", href: "/blog/weblog"}
      ],
      //表格字段
      columns: [
        {title: '博客标题', key: 'weblogTitle', dataIndex: 'weblogTitle', sorter: true,},
        {title: '标题图片', key: 'weblogTitleImage', dataIndex: 'weblogTitleImage'},
        {title: '文章文本', key: 'weblogText', dataIndex: 'weblogText', ellipsis: "true",},
        {title: '浏览量', key: 'pageView', dataIndex: 'pageView', sorter: true,},
        {title: '类型', key: 'type', dataIndex: 'type', sorter: true},
        {title: '状态', key: 'status', dataIndex: 'status', sorter: true,},
        {title: '操作', dataIndex: 'operation', fixed: 'right', width: "100px",},
      ],
      //form中的字段
      formColumns: [
        ModalField.init('博客标题', 'weblogTitle', InputType.String),
        ModalField.init('标题图片', 'weblogTitleImage', InputType.Image),
        ModalField.init('文章文本', 'weblogText', InputType.Textarea),
        ModalField.init('类型', 'type', InputType.Select, <Ext>{selectParameter: {dictName: '博客类型'}}),
        ModalField.init('状态', 'status', InputType.Select, <Ext>{selectParameter: {dictName: '博客状态'}}),
      ],
      //fomr校验规则
      rules: {
        dictName: [
          {required: true, type: 'string', trigger: 'change', message: "字典名称不可为空"},
        ],
        dictKey: [
          {required: true, type: 'string', trigger: 'change', message: "字典KEY不可为空"},
        ],
        dictValue: [
          {required: true, type: 'string', trigger: 'change', message: "字典值不可为空"},
        ],
      },
      //校验的原始值
      check: {
        roleName: "",
      }
    }
  },
  methods: {
    openAdd(parentId: string) {
      const form: any = this.$refs.form
      form.open({parentId: parentId})
      const check: any = this.check
      check.roleName = undefined;
    },
    openEdit(id: string) {
      const form: any = this.$refs.form
      form.open({id: id})
      // setTimeout(() => {
      //   this.check.roleName = this.$refs.form.getData().roleName;
      // }, 1000)
    },
    del(id: string) {
      this.$modal.confirm({
        title: '删除',
        icon: createVNode(this.$icons["ExclamationCircleOutlined"]),
        content: '确定要删除吗？',
        onOk: () => {
          return WeblogApi.del([id]).then(() => {
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
    const tablePageOption = PageInfo(WeblogApi)

    let weblogStatus = ref([])
    onMounted(async () => {
      const res = await SysDictApi.getSelect('博客状态')
      weblogStatus.value = res.data.data
    });
    let weblogType = ref([])
    onMounted(async () => {
      const res = await SysDictApi.getSelect('博客类型')
      weblogType.value = res.data.data
    });

    return {
      weblogStatus, weblogType,
      ...tablePageOption
    }
  }
})

</script>