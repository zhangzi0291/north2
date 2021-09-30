<style lang="less">

</style>
<template>
  <base-page :breadcrumbs="breadcrumbs">
    <template #content>
      <div>
        <a-row>
          <a-col :offset="16" :span="2">
            <a-button type="primary" @click="getGenealogyTreeData(1)">刷新</a-button>
          </a-col>
          <a-col :span="2">
            <a-button type="primary" @click="openPeson">新增</a-button>
          </a-col>

        </a-row>
        <a-row>
          <a-col :span="24" style="height:500px">
            <genealogy-tree ref="genealogyTree"/>
          </a-col>
        </a-row>

        <show-person ref="showPerson" :okCallback="okCallback"></show-person>
      </div>
    </template>
  </base-page>

</template>

<script lang="ts">
import showPerson from './ShowPerson.vue';
import GenealogyTree from './GenealogyTree.vue';
import {defineComponent} from "vue";

const echarts = require('echarts');
export default defineComponent({
  name: "GenealogyTreePage",
  components: {
    showPerson, GenealogyTree
  },
  data() {
    return {
      breadcrumbs: [
        {name: "", icon: "HomeOutlined", href: "/home"},
        {name: "家族关系", icon: "", href: "/genealogy/tree"},
      ],
      genealogyTree: {},
      chart: {},
    }
  },
  computed: {},
  methods: {
    getGenealogyTreeData(id: string) {
      const genealogyTree: any = this.$refs.genealogyTree
      genealogyTree.getGenealogyTreeData(id)
    },
    openPeson() {
      const showPerson: any = this.$refs.showPerson
      showPerson.open(null, true)
    },
    okCallback() {
      this.getGenealogyTreeData("1")
    },

  },
  mounted() {
    this.getGenealogyTreeData("1");

  }

})

</script>
