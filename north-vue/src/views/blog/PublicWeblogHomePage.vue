<style lang="less">
.logo {
  height: 32px;
  padding: 10px;
  border-radius: 5px;
  background: rgba(255, 255, 255, 0.2);
  margin: 16px;
  color: rgba(255, 255, 255, 0.8);
  text-align: center;
  line-height: 32px;
}

.content {
  display: flex;
  justify-content: center;
  overflow-y: auto;
}

.blog-content {
  min-height: 100%;
  padding: 5px;
}

.blog-other {
  padding: 5px;
}

.weblog-box {
  background-color: #f0f2f5;
}
</style>
<template>
  <a-list item-layout="vertical" :loading="loading" style="width:70%;margin-top: 10px" :data-source="data">
    <template #header>
      <a-form layout="inline">
        <a-form-item label="标题搜索">
          <a-input v-model:value="search.weblogTitle" @change="searchData" @pressEnter="searchData" allowClear/>
        </a-form-item>
        <a-form-item label="发布时间">
          <a-range-picker :defaultValue="search.publicDateTime" show-time
                          @change="onDateTimeChange($event,'publicDateTime')" @ok="onDateTimeOk"/>
        </a-form-item>
      </a-form>
    </template>
    <template #renderItem="{ item }">
      <a-list-item>
        <blog-cell :item="item" :user-id="$route.params.userId"></blog-cell>
      </a-list-item>
    </template>
    <template #loadMore>
      <div :style="{ textAlign: 'center', margin: '12px 0', height: '32px', lineHeight: '32px' }">
        <a-spin v-if="loading"/>
        <template v-else>
          <a-button v-if="showMore" @click="loadMore">加载更多</a-button>
        </template>
      </div>
    </template>
  </a-list>
</template>
<script lang="ts">
import FormModal from "@/components/base/FormModal.vue";
import MenuModal from "@/views/sys/sysrole/MenuModal.vue";
import {defineComponent, reactive, ref} from "vue";
import WeblogApi from "@/api/WeblogApi";
import BlogCell from "@/views/blog/BlogCell.vue";
import {Moment} from "moment";
import debounce from "lodash/debounce"

export default defineComponent({
  name: 'PublicWeblogHomePage',
  components: {
    FormModal, MenuModal, BlogCell
  },
  data() {
    return {
      //接口url
      logoName: "XXXX博客",
      loading: false,
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
      search: {},
      areaLevels: [],
      //表格字段
      columns: [
        {title: '博客标题', key: 'weblogTitile', dataIndex: 'weblogTitile', sorter: true,},
        {
          title: '标题图片',
          key: 'weblogTitleImage',
          slots: {customRender: 'weblogTitleImage'},
          dataIndex: 'weblogTitleImage'
        },
        {title: '文章文本', key: 'weblogText', dataIndex: 'weblogText', ellipsis: "true",},
        {title: '浏览量', key: 'pageView', dataIndex: 'pageView', sorter: true,},
        {title: '操作', dataIndex: 'operation', slots: {customRender: 'operation'}, fixed: 'right', width: "100px",},
      ],
      //校验的原始值
      check: {
        roleName: "",
      }
    }
  },
  methods: {
    isMe() {
      return this.$route.params.userId == this.user.userId
    },
    openAdd(parentId: string) {
      const form: any = this.$refs.form
      form.open({parentId: parentId})
      const check: any = this.check
      check.roleName = undefined;
    },
    openEdit(id: string) {
      const form: any = this.$refs.form
      form.open({id: id})
    },
    loadMore() {
      this.page.current = this.page.current + 1
      this.load()
    },
    onDateTimeChange(dates: Array<Moment>) {
      if (dates.length > 0) {
        this.search['startDate'] = dates[0].toDate().Format('yyyy-MM-dd hh:mm:ss')
        this.search['endDate'] = dates[1].toDate().Format('yyyy-MM-dd hh:mm:ss')
      } else {
        this.search['startDate'] = ""
        this.search['endDate'] = ""
        this.searchData()
      }
    },
    onDateTimeOk() {
      this.searchData()
    },
    searchData:
        debounce(function (e) {
          this.page.current = 1
          this.loading = true
          WeblogApi.publicList(this.search, this.page, this.sort).then(res => {
            // data.value.length = 0
            this.data = res.data.data.records
            this.page.current = res.data.data.current
            this.page.total = res.data.data.total
            this.loading = false
            if (res.data.data.records.length == 0) {
              this.showMore = false
            }
          })
        }, 1000)
  },
  created() {
    this.load()
    this.isMe()
  },
  setup() {
    let showMore = ref(true)
    //表格加载状态
    let loading = ref(false)
    //分页
    let page = reactive({current: 1, total: 0, size: 10})
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
      WeblogApi.publicList(search, page, sort).then(res => {
        // data.value.length = 0
        data.value = data.value.concat(res.data.data.records)
        page.current = res.data.data.current
        page.total = res.data.data.total
        loading.value = false
        if (res.data.data.records.length == 0) {
          showMore.value = false
        }
      })
    }
    const tableChange = function (pageParam: any, filters: any, sorter: any) {
      page.current = pageParam.current
      page.total = pageParam.total
      sort.field = sorter.field
      sort.order = sorter.order
      load()
    }
    const tablePageOption = {showMore, loading, data, page, search, load, tableChange}

    return {
      ...tablePageOption
    }
  }
})

</script>