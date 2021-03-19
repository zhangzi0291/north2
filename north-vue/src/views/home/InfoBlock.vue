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
    <a-statistic :title="title" :value="number"  >
      <template #prefix>
        <component :is="icon" />
      </template>
    </a-statistic>
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

export default class InfoBlock extends Vue {
}
</script>

