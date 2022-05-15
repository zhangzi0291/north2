<style lang="less">

</style>
<template>
  <div>
    <base-page :breadcrumbs="breadcrumbs">
      <template #content>
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
          <a-form layout="inline">
            <a-form-item label="博客标题">
              <a-input v-model:value="search.weblogTitle" allowClear/>
            </a-form-item>
          </a-form>
        </a-row>
        <a-table :columns="columns" :data-source="data" :loading="loading" :rowKey="(record)=>record.id"
                 :scroll="{ x: 900, y: 210 }" :pagination="page" @change="tableChange"
                 bordered>
          <template #weblogTitleImage="{ record }">
            <a-image :width="50"
                     :src="BASE_URL+'/sysFile/download?id='+record.weblogTitleImage"
            />
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
                :okCallback="load" :rules="rules" :title="'字典'">
      <template #resources="{data}">
        <a-button type="primary" @click="openMenuModal(data)">分配资源</a-button>
      </template>
    </form-modal>

  </div>
</template>
<script lang="ts">
import FormModal, {Ext, InputType, ModalField} from "@/components/base/FormModal.vue";
import MenuModal from "@/views/sys/sysrole/MenuModal.vue";
import {createVNode, defineComponent, reactive, ref} from "vue";
import {AxiosResponse} from "axios";
import SysDictApi from "@/api/SysDictApi";
import WeblogApi from "@/api/WeblogApi";


export default defineComponent({
  name: 'Weblog',
  components: {
    FormModal, MenuModal
  },
  data() {
    let weblogStatus = [];
    SysDictApi.getSelect('博客状态').then((res: AxiosResponse) => {
      weblogStatus = res.data.data
    });
    let weblogType = [];
    SysDictApi.getSelect('博客类型').then((res: AxiosResponse) => {
      weblogType = res.data.data
    });

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
        {
          title: '标题图片',
          key: 'weblogTitleImage',
          slots: {customRender: 'weblogTitleImage'},
          dataIndex: 'weblogTitleImage'
        },
        {title: '文章文本', key: 'weblogText', dataIndex: 'weblogText', ellipsis: "true",},
        {title: '浏览量', key: 'pageView', dataIndex: 'pageView', sorter: true,},
        {
          title: '类型', key: 'type', dataIndex: 'type', sorter: true,
          customRender: function (record: any) {
            for (let dict of weblogType) {
              let type = (<any>dict)
              if (type.value == record.record.type) {
                return type.lable
              }
            }
            return "未知"
          }
        },
        {
          title: '状态', key: 'status', dataIndex: 'status', sorter: true,
          customRender: function (record: any) {
            for (let dict of weblogStatus) {
              let type = (<any>dict)
              if (type.value == record.record.status) {
                return type.lable
              }
            }
            return "未知"
          }
        },
        {title: '操作', dataIndex: 'operation', slots: {customRender: 'operation'}, fixed: 'right', width: "100px",},
      ],
      //form中的字段
      formColumns: [
        ModalField.init('博客标题', 'weblogTitle', InputType.String),
        ModalField.init('标题图片', 'weblogTitleImage', InputType.Image),
        ModalField.init('文章文本', 'weblogText', InputType.Textarea),
        ModalField.init('类型', 'type',  InputType.Select, <Ext>{selectParameter:{dictName:'博客类型'}}),
        ModalField.init('状态', 'status', InputType.Select, <Ext>{selectParameter:{dictName:'博客状态'}}),
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
          return WeblogApi.del([id]).then(res => {
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
      WeblogApi.list(search, page, sort).then(res => {
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