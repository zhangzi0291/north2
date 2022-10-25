<style lang="less">
.index-card {
  border-radius: 0.5rem;
  width: 80%;
  box-shadow: 0 4px 10px rgba(0, 2, 4, 0.06), 0 0 rgba(0, 2, 4, 0.11);
  background-color: #ffffff;
  margin: auto auto 20px;
  padding: 5px 28px 28px;
}

.index-header {
  margin: 15px 0;

  &-title {
    font-size: 40px;
  }

  &-toolbar {
    float: right;
  }
}

.post-metas {
  color: #d0d0d0;
}

.index-excerpt-text {
  margin: 10px 0;
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
          <div class="index-header">
            <span class="index-header-title">{{ item.weblogTitle }}</span>
            <div class="index-header-toolbar">
              <template v-if="item.canEdit">
                <a-button @click="toEdit">编辑</a-button>
              </template>
            </div>
          </div>
          <div class="index-btm post-metas">
            <a-space>
              <span>
                <EyeOutlined/>
                {{ item.pageView ? item.pageView : 0 }}
              </span>
              <time :datetime="item.createdTime">
                {{ item.createdTime }}
              </time>
            </a-space>
          </div>
          <div class="index-excerpt">
            <a-image v-if="item.weblogTitleImage" :src="BASE_URL+'/sysFile/download?id='+item.weblogTitleImage"
                     style="width: 100%"
            />
            <template v-if="item.weblogText">
              <md-editor :modelValue="item.weblogText" previewOnly/>
            </template>
          </div>

        </article>
      </div>
    </template>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";

export default defineComponent({
  name: "BlogDetail ",
  data() {
    return {
      text: "",
    }
  },
  props: {
    item: {
      type: Object,
      required: true
    }
  },
  methods: {
    openBlogDetail() {
      console.log("open detail", this.item.id)
    },
    toEdit() {
      this.$router.push({name: 'publicWeblogUpdate', params: {id: this.item.id}})
    }
  },
  mounted() {
    this.text = this.item.weblogText
  },
})
</script>

