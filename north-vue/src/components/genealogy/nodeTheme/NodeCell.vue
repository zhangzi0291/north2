<template>
  <div class="node ">
123
  </div>
</template>

<script lang="ts">

import {defineComponent, inject} from "vue";

export default defineComponent({

  name: 'NodeCell',
  data() {
    return {
      node:{} as any,
      data: {},
      configType:"",
      status: 'blank',
      loading:false,
    }
  },
  watch:{

  },
  methods: {

  },
  mounted() {
    // const node = (inject('getNode') as any)()
    // this.node = node
    // if(node.data.data.logoName){
    //   this.logo = node.data.data.logoName
    // }
    // this.label = node.data.label
    // this.configType = node.data.name
    //
    // // 监听数据改变事件
    // node.on('change:data', (data: any) => {
    //   const current = data.current
    //   this.label = current.label
    //   this.status = current.status
    // })
  },
})
</script>
<style lang="less">
.node{
  width: 150px;
  height: 200px;
  border-radius: 10px;
  background-color: #fff;
  filter: drop-shadow(-2px 2px 8px rgba(50, 50, 0, 0.5));
}
</style>
