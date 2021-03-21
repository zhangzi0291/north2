<style lang="less">
.title-font {
  font-size: 16px;
}

.number-font {
  font-size: 22px;
}
</style>
<template>
  <a-card style="width: 200px">
    <a-space align="center" direction="vertical">
      <a-progress :percent="percent" :strokeColor="strokeColor" :strokeWidth="15"
                  gapPosition="left" type="dashboard"/>
      {{title}}
    </a-space>
  </a-card>
</template>

<script lang="ts">
import {Options, Vue} from 'vue-class-component';

@Options({
  name: "InfoBlock",
  data() {
    return {}
  },
  props: {
    title: {
      type: String,
    },
    percent: {
      type: Number,
    },
    strokeColor:{
      type: String,
    }
  },
})

export default class ProgressBlock extends Vue {
}
</script>

