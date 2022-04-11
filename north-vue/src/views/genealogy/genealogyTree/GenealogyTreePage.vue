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
  <base-page :breadcrumbs="breadcrumbs">
    <template #content>

      <div ref="containerPane" style="height: 100%;width: 100%">
        <div id="container" ref="container" class="flow-container" @drop="drop"></div>
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
    </template>
  </base-page>

</template>

<script lang="ts">
import GenealogyTree from './GenealogyTree.vue';
import {createVNode, defineComponent} from "vue";
import {Edge, Graph, Node, NodeView} from "@antv/x6";
import dagre from 'dagrejs'

const echarts = require('echarts');

export default defineComponent({
  name: "GenealogyTreePage",
  components: {
    GenealogyTree
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
    init() {
      const container = this.$refs.container as HTMLDivElement
      this.container = container as HTMLDivElement
      this.graph = new Graph({
        container: container,
        history: true,
        keyboard: true,
        clipboard: true,
        snapline: true,
        panning: {
          enabled: true,
          eventTypes: ['leftMouseDown']
        },
        // autoResize: this.$refs.containerPane as HTMLDivElement,
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
          connector: 'algo-connector',
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

      graph.on('edge:contextmenu', ({e, x, y, edge, view}) => {
        this.showContextMenu = true
        this.$nextTick(() => {
          const p = graph.localToPage(x, y)
          const contextMenu: any = this.$refs.contextMenu
          contextMenu.initFn(p.x, p.y, {type: 'edge', item: edge})
        })
      })

      graph.on('node:contextmenu', ({e, x, y, node, view}) => {
        this.showContextMenu = true
        this.$nextTick(() => {
          this.node = node
          const p = graph.localToPage(x, y)
          const contextMenu: any = this.$refs.contextMenu
          contextMenu.initFn(p.x, p.y, {type: 'node', item: node})
        })
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
          // this.$refs.dialogCondition.visible = true
          // this.$refs.dialogCondition.init(source.data, edge)
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

      window.addEventListener("resize", () => {

        this.resizeContainer()
        // this.graph.resize(
        //     flowPane.clientWidth - 20  ,
        //     flowPane.clientHeight - 20
        // );
        // // 所有节点 适配
        // this.devs.forEach((item:any) => {
        //   // 注册时就是用的 设备id
        //   const node = this.graph.getCellById(item._id);
        //   if (node) {
        //     // 拿到 当前节点等 宽高 坐标 的Rem 信息
        //     const { widthRem, heightRem, xRem, yRem } = node.getData();
        //     // 设置节点的宽高
        //     node.resize(widthRem * currentFontSize, heightRem * currentFontSize);
        //     // 设置节点的位置
        //     node.position(xRem * currentFontSize, yRem * currentFontSize);
        //   }
        // });
      });

    },
  },
  mounted() {
    this.init()

  }

})

</script>
