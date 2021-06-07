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
      <a-progress :percent="percent" :strokeColor="getDashboardColor(percent)" :strokeWidth="15"
                  gapPosition="left" type="dashboard">
        <template #format="percent">
          <span :style="{color: getDashboardColor(percent)}">{{ percent }}</span>
        </template>
      </a-progress>

      <b>{{title}}</b>
    </a-space>
  </a-card>
</template>

<script lang="ts">
import {Options, Vue} from 'vue-class-component';

@Options({
  name: "InfoBlock",
  data() {
    return {
      dashboardColor: {
        normal: "#2db7f5",
        warning: "#ff9900",
        danger: "#ed4014",
      },
    }
  },
  props: {
    title: {
      type: String,
    },
    percent: {
      type: Number,
    },
  },
  methods:{
    getDashboardColor(percent: number) {
      if (percent > 85) {
        return this.dashboardColor.danger
      } else if (percent > 60) {
        return this.dashboardColor.warning
      } else if (percent < 60) {
        return this.dashboardColor.normal
      }
      return this.dashboardColor.normal
    }
  }
})

export default class ProgressBlock extends Vue {
}
</script>

