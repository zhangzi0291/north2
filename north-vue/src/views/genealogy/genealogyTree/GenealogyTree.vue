<style lang="less">

</style>
<template>
<div  style="width:100%;height:100%">
  <div ref="chart" style="width:100%;height:100%"></div>
  <show-person ref="showPerson" :okCallback="okCallback"></show-person>
</div>

</template>

<script lang="ts">
import {Options, Vue} from 'vue-class-component';
import showPerson from './ShowPerson.vue';
import GenealogyApi from '@/api/GenealogyApi';
import {defineComponent} from "vue";

const echarts = require('echarts');

export default defineComponent({
  name: "GenealogyTree",
  components: {
    showPerson
  },
  data() {
    return {
      genealogyTree: <any>{},
      chart: <any>{},
    }
  },
  computed: {},
  methods: {
    getGenealogyTreeData(id: string) {
      GenealogyApi.getGenealogyTree(id).then(res => {
        this.genealogyTree = res.data.data
        this.initGenealogyTree()
      });
    },
    getGenealogyPersonData(id: string) {
      GenealogyApi.getGenealogyPerson(id).then(res => {
        // this.personData = res.data.data
      });
    },
    okCallback(){
      this.getGenealogyTreeData("1")
    },
    initGenealogyTree() {
      this.chart = echarts.init(this.$refs.chart);
      let layoutAnimation = this.genealogyTree.nodes.length > 100 ? false : true

      for (let index in this.genealogyTree.nodes) {
        let node = this.genealogyTree.nodes[index]
        node.name = node.personName
      }

      let $this = this

      let option = {
        title: {text: '家族关系'},
        tooltip: {
          enterable: true,
        },
        legend: [{
          data: ["家族关系"]
        }],
        series: [
          {
            name: '家族关系',
            type: 'graph',
            layout: 'force',
            nodes: this.genealogyTree.nodes,
            links: this.genealogyTree.links,
            // categories: this.genealogyTree.categories,
            roam: true,
            legendHoverLink: true,
            edgeLength: [200, 2000],
            edgeSymbol: ['none', 'arrow'],
            force: {
              repulsion: 20,//斥力
              gravity: 0.001,//引力
              edgeLength: [40, 100],//默认距离
              layoutAnimation: layoutAnimation,
            },
            symbolSize: (value: number, params: any) => {
              return 20;
            },
            //节点上的文本
            label: {
              show: true,
              position: 'bottom',
              formatter: '{b}',
            },
            //线上的文本
            edgeLabel: {
              show: false,
            },
            lineStyle: {
              color: 'source',
              shadowColor: 'rgba(0,0,0,0.5)',
              shadowBlur: 10,
              curveness:0.3,
            },
            emphasis: {
              focus: 'adjacency',
              lineStyle: {
                width: 10
              }
            },
            tooltip: {
              position: "right",
              padding: 20,
              formatter: function (params: any) {
                let data = params.data
                if (params.dataType == 'node') {
                  return "点击节点查看详情"
                } else if (params.dataType == 'edge') {
                  return data.relationship
                }
              }
            }
          }
        ]
      };

      this.chart.on('click', function (params: any) {
        let dataType = params.dataType
        if (dataType == 'node') {
          const showPerson:any = $this.$refs.showPerson
          showPerson.open(params.data);
        }
      })

      this.chart.setOption(option, true)

      window.addEventListener('resize', () => {
        this.chart.resize()
        this.chart.setOption(option, true)

      }, false);
    }
  },
  created() {
    this.getGenealogyTreeData("1");
  },
  mounted() {

  }
})

</script>
