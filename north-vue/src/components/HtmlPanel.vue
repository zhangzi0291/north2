<style scoped>

</style>

<template>
  <div>
    <iframe id="iframe" :src="url" frameborder="0" width='100%' height='100%' onload="SetCwinHeight()" ></iframe>
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
  methods: {

  },
  mounted() {
  },
})
</script>

