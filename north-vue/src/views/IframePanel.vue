<style lang="less">

</style>
<template>
  <base-page >
    <template #content>
      <html-panel :url.asyc= "$route.query.url" style="width: 100%;height: 100%"></html-panel>
    </template>
  </base-page>

</template>

<script lang="ts">
import {Options, Vue} from 'vue-class-component';
import InfoBlock from "@/views/home/InfoBlock.vue";
import HomeApi from "@/api/HomeApi";

@Options({
  name: "IframePanel",
  data() {
    return {
    }
  },
  methods: {

  },
  created() {
  }
})
export default class IframePanel extends Vue {
}
</script>
