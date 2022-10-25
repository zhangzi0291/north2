<style lang="less">
</style>
<template>
  <a-card style="width: 200px">
    <a-statistic v-model:value="number" :title="title">
      <template #prefix>
        <component :is="icon"/>
      </template>
    </a-statistic>
  </a-card>
</template>

<script lang="ts">
import {defineComponent} from "vue";


export default defineComponent({
  name: "InfoBlock",
  data() {
    return {}
  },
  props: {
    icon: {
      type: String,
    },
    title: {
      type: String,
    },
    number: {
      type: Number,
    }
  },
})
</script>

