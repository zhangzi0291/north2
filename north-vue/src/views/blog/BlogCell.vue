<style lang="less">
.index-card {
  border-radius: 0.5rem;
  margin: auto;
  box-shadow: 0 4px 10px rgba(0, 2, 4, 0.06), 0 0 rgba(0, 2, 4, 0.11);
  padding: 28px;
  margin-bottom: 38px;
  padding-top: 5px;
  background-color: #ffffff;
  margin-bottom: 20px;
}

.index-header {
  margin: 15px 0;
  font-size: 32px;
}

.index-title {
  cursor: pointer;

  &:hover {
    color: #1890ff;
  }
}

.post-metas {
  color: #d0d0d0;
}

.index-excerpt-text {
  margin: 10px 0;
}

.title-image {
  width: auto;
  max-height: 200px;
}
</style>
<template>
  <div class="weblog-card">
    <template v-if="!item.weblogTitle">
      <div class="index-card">
        <a-skeleton/>
      </div>
    </template>
    <template v-else>
      <div class="index-card">
        <article class="index-card-info">
          <h3 class="index-header">
            <span class="index-title" @click="openBlogDetail(item.id)">{{ item.weblogTitle }}</span>
            <!--          <span class="index-pin">[TOP]</span>-->
          </h3>
          <div class="index-excerpt">
            <a-image v-if="item.weblogTitleImage" class="title-image"
                     :src="BASE_URL+'/sysFile/download?id='+item.weblogTitleImage"
            />
            <div class="index-excerpt-text">
              <!--              {{ item.weblogText }}-->
              <md-editor :modelValue="item.weblogText" previewOnly/>
            </div>
          </div>
          <div class="index-btm post-metas">
            <a-space>
              <span>
                <EyeOutlined/>
                {{ item.pageView ? item.pageView : 0 }}
              </span>
              <time :datetime="item.createdTime" pubdate>
                {{ item.createdTime }}
              </time>
            </a-space>
          </div>
        </article>
      </div>
    </template>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";

export default defineComponent({
  name: "BlogCell",
  data() {
    return {}
  },
  props: {
    item: {
      type: Object,
      required: true
    },
    userId: {
      type: String,
      default: ""
    }
  },
  methods: {
    openBlogDetail(weblogId: string) {
      this.$router.push({path: '/weblog/' + this.$route.params.userId + '/' + weblogId,})
    }
  },
  mounted() {
  },
})
</script>

