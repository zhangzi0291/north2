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

.weblog-box {
  background-color: #f0f2f5;
}

.toolbar {
  position: absolute;
  top: 1em;
  right: 1em;
  z-index: 10;
}
</style>
<template>
  <div style="width:100%;min-height: 100%;">
    <div class="toolbar">
      <a-button @click="back">返回</a-button>
    </div>
    <a-row style="min-height: 100%;">
      <a-col :span="24" class="blog-content">
        <a-card :bordered="false" class="weblog-box" style="min-height: 100%;">
          <blog-detail :item="data"></blog-detail>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import {Ext, InputType, ModalField} from "@/components/base/FormModal.vue";
import BlogDetail from "@/views/blog/BlogDetail.vue";
import WeblogApi from "@/api/WeblogApi";


export default defineComponent({
  name: 'PublicWeblogDetailPage',
  components: {
    BlogDetail
  },
  data() {
    return {
      //接口url
      logoName: "XXXX博客",
      url: {
        get: "/weblog/get",
        add: "/weblog/addWeblog",
        edit: "/weblog/editWeblog",
      },
      data: {},
      //面包屑
      breadcrumbs: [
        {name: "", icon: "HomeOutlined", href: "/home"},
        {name: "博客后台", icon: "KeyOutlined", href: "/blog/weblog"}
      ],
      //form中的字段
      formColumns: [
        ModalField.init('博客标题', 'weblogTitile', InputType.String),
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
    isMe() {
      return this.$route.params.userId == this.user.userId
    },
    back() {
      this.$router.push({path: '/weblog/' + this.$route.params.userId})
    }
  },
  created() {
    WeblogApi.get(this.$route.params.weblogId).then(res => {
      this.data = res.data.data
    })
  },
})

</script>