<style lang="less">
.drag-div {
  cursor: move;

  &:hover {
    color: #1890ff;
  }
}
</style>
<template>
  <div class="drag-div" draggable="true"
       @dragend="dragend(item)">
    <UnorderedListOutlined/>
    &nbsp
    <slot></slot>
  </div>
</template>
<script lang="ts">
import {defineComponent} from "vue";

const mouseXY = {'x': 0, 'y': 0}

export interface DragItem {
  type: string;
  label: string;
  name: string;
  data: any;
}


export default defineComponent({
  name: "DragDiv",
  props: {
    target: {
      type: Object,
      required: true
    },
    item: {
      type: Object,
      required: true
    }
  },
  methods: {
    drag() {
      const parentRect = this.target.getBoundingClientRect()
      let mouseInGrid = false
      if (((mouseXY.x > parentRect.left) && (mouseXY.x < parentRect.right)) && ((mouseXY.y > parentRect.top) && (mouseXY.y < parentRect.bottom))) {
        mouseInGrid = true
      }
    },
    dragend(item: any) {
      const parentRect = this.target.getBoundingClientRect()
      let mouseInGrid = false
      if (((mouseXY.x > parentRect.left) && (mouseXY.x < parentRect.right)) && ((mouseXY.y > parentRect.top) && (mouseXY.y < parentRect.bottom))) {
        mouseInGrid = true
      }
      if (mouseInGrid === true) {
        this.$emit('addNode',
            this.nodeConfig(item, mouseXY.x, mouseXY.y)
        )
      }
    },
    drop() {
      console.log("drop")
    },
    nodeConfig(item: any, x: number, y: number) {
      let config = {}
      const time = new Date().getTime()

      // 链接桩3种状态 1、in | 只允许被连  2、out | 只允许输出  3、any | 不限制
      switch (item.type) {
        case 'input':
          config = {
            x: x,
            y: y,
            width: 120,
            height: 120,
            shape: 'dag-output',
            data: item,
            ports: {
              groups: {
                bottom: {
                  position: 'bottom',
                  attrs: {
                    circle: {
                      r: 4,
                      magnet:
                          true,
                      stroke: '#C2C8D5',
                      strokeWidth: 1,
                      fill: '#fff'
                    }
                  }
                }
              },
              items: [
                {
                  id: `out-${time}`,
                  group: 'bottom' // 指定分组名称
                }
              ]
            }
          }
          break;
        case 'output':
          config = {
            x: x,
            y: y,
            width: 120,
            height: 120,
            shape: 'dag-onlyIn',
            data: item,
            ports: {
              groups: {
                top: {
                  position: 'top',
                  attrs:
                      {
                        circle: {
                          r: 4,
                          magnet: true,
                          stroke:
                              '#C2C8D5',
                          strokeWidth: 1,
                          fill: '#fff'
                        }
                      }
                },
              },
              items: [
                {
                  id: `in-${time}`,
                  group: 'top' // 指定分组名称
                }
              ]
            }
          }
          break;
        case 'condition' :
          config = {
            x: x,
            y: y,
            width: 180,
            height: 40,
            shape: 'dag-condition',
            data: item,
            ports: {
              groups: {
                top: {
                  position: 'top',
                  attrs:
                      {
                        circle: {
                          r: 4,
                          magnet: true,
                          stroke:
                              '#C2C8D5',
                          strokeWidth: 1,
                          fill: '#fff'
                        }
                      }
                },
                bottom: {
                  position: 'bottom',
                  attrs: {
                    circle: {
                      r: 4,
                      magnet:
                          true,
                      stroke: '#C2C8D5',
                      strokeWidth: 1,
                      fill: '#fff'
                    }
                  }
                }
              },
              items: [
                {
                  id: `in-${time}`,
                  group: 'top' // 指定分组名称
                },
                {
                  id: `out-${time}`,
                  group: 'bottom' // 指定分组名称
                },
                {
                  id: `out-${time}-2`,
                  group: 'bottom' // 指定分组名称
                }
              ]
            }
          }
          break;
        default :
          config = {
            x: x,
            y: y,
            width: 120,
            height: 120,
            shape: 'dag-node',
            data: item,
            ports: {
              groups: {
                top: {
                  position: 'top',
                  attrs:
                      {
                        circle: {
                          r: 4,
                          magnet: true,
                          stroke:
                              '#C2C8D5',
                          strokeWidth: 1,
                          fill: '#fff'
                        }
                      }
                },
                bottom: {
                  position: 'bottom',
                  attrs: {
                    circle: {
                      r: 4,
                      magnet:
                          true,
                      stroke: '#C2C8D5',
                      strokeWidth: 1,
                      fill: '#fff'
                    }
                  }
                }
              },
              items: [
                {
                  id: `in-${time}`,
                  group: 'top' // 指定分组名称
                },
                {
                  id: `out-${time}`,
                  group: 'bottom' // 指定分组名称
                }
              ]
            }
          }
          break;

      }
      return config
    },
  },
  mounted() {
    window.addEventListener('dragover', function (e) {
      mouseXY.x = e.clientX
      mouseXY.y = e.clientY
    }, false)
  }
})
</script>