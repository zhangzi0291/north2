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
  position: relative;
  display: flex;
  justify-content: center;
  overflow-y: auto;
}
</style>
<template>
  <a-layout style="height:100%">
    <a-layout-header>
        <span class="logo">
          {{ logoName }}
        </span>
    </a-layout-header>
    <a-layout-content class="content">
      <router-view/>
    </a-layout-content>
    <!--    <a-layout-footer>Footer</a-layout-footer>-->
  </a-layout>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import BlogCell from "@/views/blog/BlogCell.vue";

export default defineComponent({
  name: 'PublicWeblogPage',
  components: {
    BlogCell
  },
  data() {
    return {
      //接口url
      logoName: "XXXX博客",
    }
  },
  methods: {},
})

</script>