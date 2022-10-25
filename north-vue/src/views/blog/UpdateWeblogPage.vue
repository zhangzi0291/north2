<style lang="less">

</style>
<template>
  <div>

    <update-weblog></update-weblog>


  </div>
</template>
<script lang="ts">
import {defineComponent} from "vue";
import UpdateWeblog from "@/views/blog/UpdateWeblog.vue";


export default defineComponent({
  name: 'UpdateWeblogPage',
  components: {
    UpdateWeblog
  },
  data() {
    return {
      //面包屑
      breadcrumbs: [
        {name: "", icon: "HomeOutlined", href: "/home"},
        {name: "编辑博客", icon: "KeyOutlined", href: "/blog/updateWeblog"}
      ],
    }
  },
  methods: {},
  created() {

  },
  setup() {

  }
})

</script>