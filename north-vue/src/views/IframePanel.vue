<style lang="less">

</style>
<template>
  <html-panel :url.asyc="$route.query.url" style="width: 100%;height: 100%"></html-panel>
</template>

<script lang="ts">
import {Options, Vue} from 'vue-class-component';

@Options({
  name: "IframePanel",
  data() {
    return {}
  },
  methods: {},
  created() {
  }
})
export default class IframePanel extends Vue {
}
</script>
