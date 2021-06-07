<style lang="less">

</style>
<template>
  <base-page :breadcrumbs="breadcrumbs">
   <template #content>
      <div>
        <a-row>
          <a-col :offset="16" :span="2">
            <a-button type="primary" @click="getGenealogyTreeData(1)" >刷新</a-button>
          </a-col>
          <a-col :span="2">
            <a-button type="primary" @click="openPeson">新增</a-button>
          </a-col>

        </a-row>
        <a-row>
          <a-col :span="24" style="height:500px">
            <genealogy-tree ref="genealogyTree" />
          </a-col>
        </a-row>

        <show-person ref="showPerson" :okCallback="okCallback"></show-person>
      </div>
    </template>
  </base-page>

</template>

<script lang="ts">
import {Options, Vue} from 'vue-class-component';
import showPerson from './ShowPerson.vue';
import GenealogyTree from './GenealogyTree.vue';
import GenealogyApi from '@/api/GenealogyApi';

const echarts = require('echarts');

@Options({
  name: "GenealogyTreePage",
  components: {
    showPerson,GenealogyTree
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
      this.$refs.genealogyTree.getGenealogyTreeData(id)
    },
    openPeson() {
      this.$refs.showPerson.open(null,true)
    },
    okCallback(){
      this.getGenealogyTreeData("1")
    },

  },
  mounted() {
    this.getGenealogyTreeData("1");

  }

})
export default class GenealogyTreePage extends Vue {
}
</script>
