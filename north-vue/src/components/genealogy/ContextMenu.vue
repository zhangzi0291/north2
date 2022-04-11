<style lang="less">
.contextMenu {
  z-index: 999;
  width: 150px;

}
</style>
<template>
  <a-card size="small" class="contextMenu" :style="{position: 'absolute',left: x+'px',top: y+'px'}">
    <template v-if="item.type!='edge'">
      <a-row>
        <a-button block @click="callBack('setConfig')">配置参数</a-button>
      </a-row>
      <a-row v-if="hasAssessment">
        <a-button block @click="callBack('assessment')" :disabled="!assessmentEnable">模型评估</a-button>
      </a-row>
      <a-row v-if="hasAssessment">
        <a-button block @click="callBack('downloadModel')" :disabled="!assessmentEnable">模型下载</a-button>
      </a-row>
    </template>
    <a-row>
      <a-button block @click="callBack('remove')">删除</a-button>
    </a-row>
  </a-card>
</template>
<script lang="ts">

import {defineComponent} from "vue";

export default defineComponent({
  name: 'ContextMenu',
  data() {
    return {
      item: {},
      hasAssessment: false,
      assessmentEnable: false,
      x: 0,
      y: 0
    }
  },
  props: {},
  methods: {
    initFn(x: number, y: number, item: any) {
      if (item.type != 'edge') {
        if (item.item.data.type == 'ML' || item.item.data.type == 'RL') {
          this.hasAssessment = true
        } else {
          this.hasAssessment = false
        }
        if (item.item.data.status == 'success') {
          this.assessmentEnable = true
        } else {
          this.assessmentEnable = false
        }
      }
      this.x = x
      this.y = y
      if (item) {
        this.item = item
      }
    },
    setData(item: any) {
      this.item = item
    },
    callBack(type: string) {
      this.$emit('callback', type, this.item)
    }
  },
})
</script>