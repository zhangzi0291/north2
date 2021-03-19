<style scoped>

</style>

<template>
  <div>
    <iframe id="iframe" :src="url" frameborder="0" width='100%' height='100%' onload="SetCwinHeight()" ></iframe>
  </div>
</template>

<script>
import {Options, Vue} from "vue-class-component";

@Options({
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
export default class HtmlPanel extends Vue {
}
</script>

