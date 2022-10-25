<style lang="less">
.contextMenu {
  z-index: 999;
  width: 150px;

}
</style>
<template>
  <a-card :style="{position: 'absolute',left: x+'px',top: y+'px'}" class="contextMenu" size="small">
    <template v-if="item.type!='edge'">
      <a-row>
        <a-button block @click="callBack('setConfig')">配置参数</a-button>
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
      x: 0,
      y: 0
    }
  },
  props: {},
  methods: {
    initFn(x: number, y: number, item: any) {
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