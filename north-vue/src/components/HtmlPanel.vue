<style scoped>

</style>

<template>
  <div>
    <iframe id="iframe" :src="url" height='100%' onload="SetCwinHeight()" width='100%'></iframe>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";

export default defineComponent({
  name: "HtmlPanel",
  data() {
    return {}
  },
  props: {
    url: {
      required: true
    }
  },
  methods: {},
  mounted() {
  },
})
</script>

