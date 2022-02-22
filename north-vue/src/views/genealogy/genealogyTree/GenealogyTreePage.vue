<style lang="less">

</style>
<template>
  <base-page :breadcrumbs="breadcrumbs">
    <template #content>
      <div>
        <!--        <a-row>-->
        <!--          <a-col :offset="16" :span="2">-->
        <!--            <a-button type="primary" @click="getGenealogyTreeData(1)">刷新</a-button>-->
        <!--          </a-col>-->
        <!--          <a-col :span="2">-->
        <!--            <a-button type="primary" @click="openPeson">新增</a-button>-->
        <!--          </a-col>-->

        <!--        </a-row>-->
        <!--        <a-row>-->
        <!--          <a-col :span="24" style="height:500px">-->
        <!--            <genealogy-tree ref="genealogyTree"/>-->
        <!--          </a-col>-->
        <!--        </a-row>-->

        <!--        <show-person ref="showPerson" :okCallback="okCallback"></show-person>-->
        <a-button @click="undo">撤销</a-button>
        <div ref="container" style="width: 100%;height: 500px"></div>
      </div>
    </template>
  </base-page>

</template>

<script lang="ts">
import showPerson from './ShowPerson.vue';
import GenealogyTree from './GenealogyTree.vue';
import {defineComponent} from "vue";
import {Edge, Graph, Node, NodeView} from "@antv/x6";
import {MyShape} from "./MyShape";
import dagre from 'dagrejs'

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
      graph: {} as Graph,
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
    undo() {
      this.graph.undo()
    },
    update(view: NodeView) {
      const cell = view.cell
      if (cell instanceof MyShape) {
        cell.getInPorts().forEach((port) => {
          console.log(port.id)
          const portNode = view.findPortElem(port.id!, 'portBody')
          view.unhighlight(portNode, {
            highlighter: MyShape.magnetAvailabilityHighlighter,
          })
        })
      }
    },
    layout() {
      const dir: string = "BT"
      const nodes = this.graph.getNodes()
      const edges = this.graph.getEdges()
      const g = new dagre.graphlib.Graph()
      g.setGraph({rankdir: dir, nodesep: 16, ranksep: 16})
      g.setDefaultEdgeLabel(() => ({}))

      const width = 260
      const height = 90
      nodes.forEach((node) => {
        g.setNode(node.id, {width, height})
      })

      edges.forEach((edge) => {
        const source = edge.getSource() as Edge.TerminalCellData
        const target = edge.getTarget() as Edge.TerminalCellData
        g.setEdge(source.cell, target.cell)
      })

      dagre.layout(g)

      this.graph.freeze()

      g.nodes().forEach((id) => {
        const node = this.graph.getCellById(id)
        if (node instanceof Node) {
          const pos = g.node(id)
          node.position(pos.x, pos.y)
        }
      })

      edges.forEach((edge) => {
        const source = edge.getSourceNode()!
        const target = edge.getTargetNode()!
        const sourceBBox = source.getBBox()
        const targetBBox = target.getBBox()

        console.log(sourceBBox, targetBBox)

        if ((dir === 'LR' || dir === 'RL') && sourceBBox.y !== targetBBox.y) {
          const gap =
              dir === 'LR'
                  ? targetBBox.x - sourceBBox.x - sourceBBox.width
                  : -sourceBBox.x + targetBBox.x + targetBBox.width
          const fix = dir === 'LR' ? sourceBBox.width : 0
          const x = sourceBBox.x + fix + gap / 2
          edge.setVertices([
            {x, y: sourceBBox.center.y},
            {x, y: targetBBox.center.y},
          ])
        } else if (
            (dir === 'TB' || dir === 'BT') &&
            sourceBBox.x !== targetBBox.x
        ) {
          const gap =
              dir === 'TB'
                  ? targetBBox.y - sourceBBox.y - sourceBBox.height
                  : -sourceBBox.y + targetBBox.y + targetBBox.height
          const fix = dir === 'TB' ? sourceBBox.height : 0
          const y = sourceBBox.y + fix + gap / 2
          edge.setVertices([
            {x: sourceBBox.center.x, y},
            {x: targetBBox.center.x, y},
          ])
        } else {
          edge.setVertices([])
        }
      })

      this.graph.unfreeze()
    },
    init() {
      const container = this.$refs.container as HTMLDivElement
      this.graph = new Graph({
        container: container,
        history: true,
        snapline: true,
        panning: {
          enabled: true,
          modifiers: ['ctrl'],
        },
        highlighting: {
          magnetAvailable: MyShape.magnetAvailabilityHighlighter,
          magnetAdsorbed: {
            name: 'stroke',
            args: {
              attrs: {
                fill: '#fff',
                stroke: '#31d0c6',
              },
            },
          },
        },
        connecting: {
          snap: true,
          allowBlank: false,
          allowLoop: false,
          highlight: true,
          router: {
            name: 'er',
            args: {
              direction: 'V',
            },
          },
          validateConnection({sourceView, targetView, targetMagnet}) {
            if (!targetMagnet) {
              return false
            }
            if (targetMagnet.getAttribute('port-group') !== 'in') {
              return false
            }
            if (targetView) {
              const node = targetView.cell
              // if (node instanceof MyShape) {
              //   const portId = targetMagnet.getAttribute('port')
              //   const usedInPorts = node.getUsedInPorts(graph)
              //   if (usedInPorts.find((port) => port && port.id === portId)) {
              //     return false
              //   }
              // }
            }
            return true
          },
        },
        background: {
          color: '#fffbe6', // 设置画布背景颜色
        },
        grid: {
          size: 10,      // 网格大小 10px
          visible: true, // 渲染网格背景
        },
        // mousewheel: {
        //   enabled: true,
        //   modifiers: ['ctrl'],
        // },
      });
      const graph: Graph = this.graph as Graph
      const data = {
        // 节点
        nodes: [
          {
            id: 'node1', // String，可选，节点的唯一标识
            x: 40,       // Number，必选，节点位置的 x 值
            y: 40,       // Number，必选，节点位置的 y 值
            width: 80,   // Number，可选，节点大小的 width 值
            height: 40,  // Number，可选，节点大小的 height 值
            attrs: {
              body: {
                fill: '#2ECC71', // 背景颜色
                stroke: 'none',  // 边框颜色
              },
              label: {
                text: 'rect',    // 文本
                fill: '#333',    // 文字颜色
                fontSize: 18,    // 文字大小
              },
            },
            ports: {
              groups: {
                in: {
                  position: 'top',    // 链接桩位置
                  attrs: {
                    circle: {
                      r: 6,
                      magnet: true,
                      stroke: '#31d0c6',
                      strokeWidth: 2,
                      fill: '#fff',
                    },
                  },
                },
                out: {
                  position: 'bottom',   // 链接桩位置
                  attrs: {
                    circle: {
                      r: 6,
                      magnet: true,
                      stroke: '#3122c6',
                      strokeWidth: 2,
                      fill: '#fff',
                    },
                  },
                },
              },
              items: [
                {
                  id: 'port1',
                  group: 'in',
                },
                {
                  id: 'port2',
                  group: 'out',
                }
              ],
            }
          },
          {
            id: 'node2', // String，节点的唯一标识
            x: 160,      // Number，必选，节点位置的 x 值
            y: 180,      // Number，必选，节点位置的 y 值
            width: 80,   // Number，可选，节点大小的 width 值
            height: 40,  // Number，可选，节点大小的 height 值
            label: 'world', // String，节点标签
            ports: {
              group: {
                in: {
                  position: 'top',
                  attrs: {
                    circle: {
                      r: 6,
                      magnet: true,
                      stroke: '#31d0c6',
                      strokeWidth: 2,
                      fill: '#fff',
                    },
                  },
                },
                out: {
                  position: 'bottom',
                  attrs: {
                    circle: {
                      r: 6,
                      magnet: true,
                      stroke: '#31d0c6',
                      strokeWidth: 2,
                      fill: '#fff',
                    },
                  },
                }
              }, // 链接桩组定义
              items: [
                {
                  id: 'port1',
                  group: "in",
                  attrs: {
                    text: {text: 'in1'},
                  },
                },
                {
                  id: 'port2',
                  group: "out",
                  attrs: {
                    text: {text: 'out1'},
                  },
                },
              ], // 链接桩
            }
          },
          {
            id: 'node3', // String，节点的唯一标识
            x: 260,      // Number，必选，节点位置的 x 值
            y: 280,      // Number，必选，节点位置的 y 值
            width: 80,   // Number，可选，节点大小的 width 值
            height: 40,  // Number，可选，节点大小的 height 值
            label: 'world123', // String，节点标签
            ports: {
              group: {
                in: {
                  position: 'top',
                  attrs: {
                    circle: {
                      r: 6,
                      magnet: true,
                      stroke: '#31d0c6',
                      strokeWidth: 2,
                      fill: '#fff',
                    },
                  },
                },
                out: {
                  position: 'bottom',
                  attrs: {
                    circle: {
                      r: 6,
                      magnet: true,
                      stroke: '#31d0c6',
                      strokeWidth: 2,
                      fill: '#fff',
                    },
                  },
                }
              }, // 链接桩组定义
              items: [
                {
                  id: 'port1',
                  group: "in",
                  attrs: {
                    text: {text: 'in1'},
                  },
                },
                {
                  id: 'port2',
                  group: "out",
                  attrs: {
                    text: {text: 'out1'},
                  },
                },
              ], // 链接桩
            }
          },
        ],
        // 边
        edges: [
          {
            source: 'node1', // String，必须，起始节点 id
            target: 'node2', // String，必须，目标节点 id

          },
          {
            source: 'node2', // String，必须，起始节点 id
            target: 'node3', // String，必须，目标节点 id

          },
        ],
      };

      // this.graph.fromJSON(data)
      this.graph.on('edge:mouseenter', ({edge}) => {
        edge.addTools([
          'target-arrowhead',
          {
            name: 'button-remove',
            args: {
              distance: -50,
            },
          },
        ])
      })
      graph.on('edge:mouseleave', ({edge}) => {
        edge.removeTools()
      })
      this.graph.on('edge:removed', ({edge, options}) => {
        console.log("removed")
        if (!options.ui) {
          return
        }

        const target = edge.getTargetCell()
        if (target instanceof MyShape) {
          target.updateInPorts(graph)
        }
      })
      graph.on('edge:connected', ({previousView, currentView}) => {
        if (previousView) {
          this.update(previousView as NodeView)
        }
        if (currentView) {
          this.update(currentView as NodeView)
        }
        this.layout()
      })
      graph.on('edge:click', ({edge}) => {
        console.log(edge.getData())
      })
      // this.graph.on('cell:click', ({ e, x, y, cell, view }) => {
      //   this.graph.getCells().forEach(c =>{
      //     c.removeTool("boundary")
      //   })
      //   cell.addTools([
      //     { name: 'boundary' },
      //   ])
      // })
      // this.graph.on('edge:contextmenu', ({ e, x, y, edge, view }) => {
      //   console.log(e,x,y,edge,view)
      // })

    }
  },
  mounted() {
    // this.getGenealogyTreeData("1");
    this.init()
    MyShape.config({
      attrs: {
        root: {
          magnet: false,
        },
        body: {
          fill: '#f5f5f5',
          stroke: '#d9d9d9',
          strokeWidth: 1,
        },
      },
      ports: {
        items: [
          {
            group: 'out',
          },
        ],
        groups: {
          in: {
            position: {
              name: 'bottom',
            },
            attrs: {
              portBody: {
                magnet: 'passive',
                r: 6,
                stroke: '#ffa940',
                fill: '#fff',
                strokeWidth: 2,
              },
            },
          },
          out: {
            position: {
              name: 'top',
            },
            attrs: {
              portBody: {
                magnet: true,
                r: 6,
                fill: '#fff',
                stroke: '#3199FF',
                strokeWidth: 2,
              },
            },
          },
        },
      },
      portMarkup: [
        {
          tagName: 'circle',
          selector: 'portBody',
        },
      ],
    })
    const parent = new MyShape({id: '1'}).init().resize(120, 40).updateInPorts(this.graph as Graph)
    parent.addChild(new MyShape({id: '2'}).init().resize(120, 40).updateInPorts(this.graph as Graph))
    parent.addChild(new MyShape({id: '3'}).init().resize(120, 40).updateInPorts(this.graph as Graph))
    this.graph.addNode(parent)
    this.graph.addEdge({
      source: '3', // String，必须，起始节点 id
      target: '1', // String，必须，目标节点 id
    })
    this.graph.addEdge({
      source: '2', // String，必须，起始节点 id
      target: '1', // String，必须，目标节点 id
      data: {
        name: "a"
      }
    })
    // this.graph.fromJSON(newModel)
    this.graph.cleanHistory()
    this.layout()
  }

})

</script>
