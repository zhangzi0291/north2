<style lang="less">
.study-pane {
  height: 100%;
}

.data-source-pane {
  padding: 5px;
  min-width: 250px;
  overflow-y: auto;
  user-select: none;
}

.flow-pane {
  position: relative;
  padding: 5px;
  min-width: 400px;
  height: 100%;
  min-height: 400px;
  flex-grow: 1;
}

.flow-container {
  width: 100%;
  height: 100%;
}

.flow-container-toolbar-top {
  position: absolute;
  left: 40px;
  top: 40px;
}

.flow-container-toolbar-right {
  position: absolute;
  right: 40px;
  top: 40px;
}

.flow-container-mini-map {
  position: absolute;
  right: 40px;
  bottom: 40px;
}

@keyframes running-line {
  to {
    stroke-dashoffset: -1000
  }
}

.ant-collapse-borderless {
  background-color: #fff
}
</style>
<template>

      <div ref="containerPane" style="display:flex; height: 100%;width: 100%">
          <div id="container" ref="container" class="flow-container" style="flex: 1" @drop="drop"></div>
        <div ref="minimap" class="flow-container-mini-map">
        </div>
        <div class="flow-container-toolbar-top">
          <a-space>
            <a-tooltip placement="bottom">
              <template #title>
                <span>撤销</span>
              </template>
              <a-button size="small" @click="undo">
                <UndoOutlined/>
              </a-button>
            </a-tooltip>
            <a-tooltip placement="bottom">
              <template #title>
                <span>反撤销</span>
              </template>
              <a-button size="small" @click="redo">
                <RedoOutlined/>
              </a-button>
            </a-tooltip>

          </a-space>
        </div>
        <div class="flow-container-toolbar-right">
          <a-space direction="vertical">
            <a-tooltip placement="left">
              <template #title>
                <span>清空</span>
              </template>
              <a-button size="small" @click="clear">
                <DeleteOutlined/>
              </a-button>
            </a-tooltip>
            <a-tooltip placement="left">
              <template #title>
                <span>居中</span>
              </template>
              <a-button size="small" @click="center">
                <PicCenterOutlined/>
              </a-button>
            </a-tooltip>
            <a-tooltip placement="left">
              <template #title>
                <span>运行</span>
              </template>
              <a-button size="small" @click="run">
                <PlayCircleOutlined/>
              </a-button>
            </a-tooltip>
            <a-tooltip placement="left">
              <template #title>
                <span>保存</span>
              </template>
              <a-button size="small" @click="save">
                <SaveOutlined/>
              </a-button>
            </a-tooltip>
            <a-tooltip placement="left">
              <template #title>
                <span>还原</span>
              </template>
              <a-button size="small" @click="restore">
                <RollbackOutlined/>
              </a-button>
            </a-tooltip>
          </a-space>
        </div>
      </div>

      <context-menu ref="contextMenu" @callback="contextMenuCallback"></context-menu>


</template>

<script lang="ts">
import GenealogyTree from './GenealogyTree.vue';
import {createVNode, defineComponent} from "vue";
import {Edge, Graph, Node, NodeView, ObjectExt} from "@antv/x6";
import NodeCell from "@/components/genealogy/nodeTheme/NodeCell.vue";
import ContextMenu from "@/components/genealogy/ContextMenu.vue";

export default defineComponent({
  name: "GenealogyTreePage",
  components: {
    GenealogyTree, NodeCell,ContextMenu
  },
  data() {
    return {
      breadcrumbs: [
        {name: "", icon: "HomeOutlined", href: "/home"},
        {name: "家族关系", icon: "", href: "/genealogy/tree"},
      ],
      graph: {} as Graph,
      node: {} as Node,
      container: {} as HTMLDivElement,
    }
  },
  computed: {},
  methods: {
    run() {

    },
    save() {
      const json = this.graph.toJSON({diff: true})
      this.$modal.confirm({
        title: '保存',
        icon: createVNode(this.$icons["ExclamationCircleOutlined"]),
        content: '确定要保存吗？',
        onOk: () => {
          this.$modal.success({
            title: '保存成功',
            content: '已保存当前视图',
          })
          window.localStorage.setItem("x6-graph", JSON.stringify(json))
          return
        },
      });

    },
    clear() {
      this.graph.removeCells(this.graph.getCells())
    },
    center() {
      this.graph.centerContent()
    },
    restore() {
      const json = JSON.parse(window.localStorage.getItem("x6-graph") + "")
      this.$modal.confirm({
        title: '还原',
        icon: createVNode(this.$icons["ExclamationCircleOutlined"]),
        content: '确定要还原吗？',
        onOk: () => {
          this.graph.fromJSON(json)
          this.center()
          return
        },
      });
    },
    keyBindFn() {
      // delete
      this.graph.bindKey(['delete'], () => {
        const cells = this.graph.getSelectedCells()
        if (cells.length) {
          this.graph.removeCells(cells)
        }
        return false
      })

      // copy
      this.graph.bindKey(['meta+c', 'ctrl+c'], () => {
        const cells = this.graph.getSelectedCells()
        if (cells.length) {
          this.graph.copy(cells)
        }
        return false
      })

      // cut
      this.graph.bindKey(['meta+x', 'ctrl+x'], () => {
        const cells = this.graph.getSelectedCells()
        if (cells.length) {
          this.graph.cut(cells)
        }
        return false
      })

      // paste
      this.graph.bindKey(['meta+v', 'ctrl+v'], () => {
        if (!this.graph.isClipboardEmpty()) {
          const cells = this.graph.paste({offset: 32})
          this.graph.cleanSelection()
          this.graph.select(cells)
        }
        return false
      })

      // undo
      this.graph.bindKey(['meta+z', 'ctrl+z'], () => {
        this.undo()
        return false
      })
      // redo
      this.graph.bindKey(['meta+y', 'ctrl+y'], () => {
        this.redo()
        return false
      })
    },
    undo() {
      if (this.graph.history.canUndo()) {
        this.graph.history.undo()
      }
    },
    redo() {
      if (this.graph.history.canRedo()) {
        this.graph.history.redo()
      }
    },
    contextMenuCallback(type){
      console.log(type)
    },
    init: function () {
      const container = this.$refs.container as HTMLDivElement
      const minimap = this.$refs.minimap as HTMLDivElement
      this.container = container as HTMLDivElement
      this.graph = new Graph({
        container: container,
        autoResize: true,
        history: true,
        keyboard: true,
        clipboard: true,
        snapline: true,
        sorting: 'approx',
        panning: {
          enabled: true,
          eventTypes: ['leftMouseDown']
        },
        minimap: {
          enabled: true,
          container: minimap,
        },
        mousewheel: {
          enabled: true,
          factor: 1.1,
          maxScale: 3,
          minScale: 0.3
        },
        selecting: {
          enabled: true,
          multiple: true,
          rubberEdge: true,
          rubberNode: true,
          modifiers: 'shift',
          rubberband: true
        },
        connecting: {
          snap: true,
          allowBlank: false,
          allowLoop: false,
          highlight: true,
          // connector: 'algo-connector',
          connectionPoint: 'anchor',
          anchor: 'center',
          validateMagnet({magnet}) {
            // return magnet.getAttribute('port-group') !== 'top'

            // 限制连线配置
            return true
          },
          createEdge() {
            return graph.createEdge({
              shape: 'dag-edge',
              attrs: {
                line: {
                  strokeDasharray: '5 5',
                  targetMarker: {
                    name: 'block',
                    width: 12,
                    height: 8
                  }
                }
              },
              zIndex: -1
            })

          },
        },
        background: {
          color: '#ffffff', // 设置画布背景颜色
        },
        grid: {
          size: 10,      // 网格大小 10px
          visible: true, // 渲染网格背景
        },
      });

      const graph: Graph = this.graph as Graph

      graph.on('blank:contextmenu', ({e, x, y}) => {
        console.log(x,y)
        const p = graph.localToClient(x,y)
        if(this.$route.path == "/genealogy/tree"){
          this.$refs.contextMenu.initFn(p.x-220,p.y-150)
        }else{
          this.$refs.contextMenu.initFn(p.x,p.y)
        }
      })

      graph.on('edge:contextmenu', ({e, x, y, edge, view}) => {
        if (edge.getTools() == undefined) {
          edge.addTools(
              [
                {
                  name: 'button-remove',
                  args: {distance: '50%'},
                },
              ],
          )
          return
        }
        const items = edge.getTools().items
        if (items.length == 0) {
          edge.addTools(
              [
                {
                  name: 'button-remove',
                  args: {distance: '50%'},
                },
              ],
          )
        } else {
          edge.removeTool('button-remove')
        }

      })

      graph.on('node:contextmenu', ({e, x, y, node, view}) => {
        var that = this

        node.addTools({
          name: 'button',
          args: {
            markup: [
              {
                tagName: 'circle',
                selector: 'button',
                attrs: {
                  r: 14,
                  stroke: '#fe854f',
                  'stroke-width': 3,
                  fill: 'white',
                  cursor: 'pointer',
                },
              },
              {
                tagName: 'text',
                textContent: 'Btn A',
                selector: 'icon',
                attrs: {
                  fill: '#fe854f',
                  'font-size': 8,
                  'text-anchor': 'middle',
                  'pointer-events': 'none',
                  y: '0.3em',
                },
              },
            ],
            x: 0,
            y: 0,
            offset: {x: 18, y: 18},
            onClick({view}) {
              console.log(view)
              console.log(this)
              that.save()
            },
          },
        })
        // this.showContextMenu = true
        // this.$nextTick(() => {
        //   this.node = node
        //   const p = graph.localToPage(x, y)
        //   const contextMenu: any = this.$refs.contextMenu
        //   contextMenu.initFn(p.x, p.y, {type: 'node', item: node})
        // })
      })

      graph.on('edge:connected', ({edge}) => {
        const source = graph.getCellById((edge.source as any).cell)
        const target = graph.getCellById((edge.target as any).cell)
        // 只允许输入
        if (target.data.type == 'input') {
          return graph.removeEdge(edge.id)
        }

        // 只允许输出
        if (source.data.type == 'output') {
          return graph.removeEdge(edge.id)
        }

        // 如果线源头的一端链接桩只允许输入
        if (/in/.test((edge.source as any).port)) {
          return graph.removeEdge(edge.id)
        }

        // 目标一端链接桩只允许输出
        if (/out/.test((edge.target as any).port)) {
          return graph.removeEdge(edge.id)
        }

        if (source.data.type == 'condition') {
          console.log(source)
          console.log(target)
          console.log(edge)
          if (target.data.t === edge.id || target.data.f === edge.id) {
            return graph.removeEdge(edge.id)
          }
        }


        source.addChild(target)

        edge.attr({
          line: {
            strokeDasharray: ''
          }
        })
      })

      graph.on('node:change:data', ({node}) => {
        const edges = graph.getIncomingEdges(node)
        const {status} = node.getData()
        edges?.forEach((edge) => {
          if (status === 'running') {
            edge.attr('line/strokeDasharray', 5)
            edge.attr('line/style/animation', 'running-line 30s infinite linear')
          } else {
            edge.attr('line/strokeDasharray', '')
            edge.attr('line/style/animation', '')
          }
        })
      })

      this.graph.addNode({
        x: 280,
        y: 100,
        width: 100,
        height: 40,
        label: 'Rect',
        ports: {
          groups: {
            top: {
              position: 'top',
              attrs: {
                circle: {
                  r: 4,
                  magnet: true,
                  stroke: '#C2C8D5',
                  strokeWidth: 1,
                  fill: '#fff',
                },
              },
            },
            bottom: {
              position: 'bottom',
              attrs: {
                circle: {
                  r: 4,
                  magnet: true,
                  stroke: '#C2C8D5',
                  strokeWidth: 1,
                  fill: '#fff',
                },
              },
            },
          },
        },
      })

      const source = this.graph.addNode({
        x: 32,
        y: 32,
        width: 100,
        height: 40,
        label: 'Hello',
      })

      const target = this.graph.addNode({
        shape: 'circle',
        x: 160,
        y: 180,
        width: 60,
        height: 60,
        label: 'World',
      })



      const source2 = this.graph.addNode({
        shape: 'PersonCell',
        x: 100,
        y: 100,
        width: 100,
        height: 40,
        label: 'Hello2',
      })
      const target2 = this.graph.addNode({
        shape: 'circle',
        x: 180,
        y: 200,
        width: 60,
        height: 60,
        label: 'World2',
        ports: {
          groups: {
            top: {
              position: 'top',
              attrs: {
                circle: {
                  r: 4,
                  magnet: true,
                  stroke: '#C2C8D5',
                  strokeWidth: 1,
                  fill: '#fff',
                },
              },
            },
            bottom: {
              position: 'bottom',
              attrs: {
                circle: {
                  r: 4,
                  magnet: true,
                  stroke: '#C2C8D5',
                  strokeWidth: 1,
                  fill: '#fff',
                },
              },
            },
          },
        },
      })

      this.graph.addEdge({
        source,
        target,
      })


    },
  },
  created() {
    this.$emit("breadcrumbs",this.breadcrumbs)
  },
  mounted() {
    this.init()

  }

})

</script>
