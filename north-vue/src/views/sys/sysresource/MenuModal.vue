<style lang="less">

</style>

<template>
  <a-modal :onCancel="cancel" :onOk="ok" :title="title" :visible="visible" :width="1080">
    <a-table :childrenColumnName="'child'" :columns="columns" :data-source="resourceList" :defaultExpandAllRows="true"
             :loading="loading" :pagination="false" :rowKey="(record)=>record.id"
             :rowSelection="rowSelection" :scroll="tableScroll">
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex == 'data.resourceName'">
          {{ record.data.resourceName }}
        </template>
        <template v-if="column.dataIndex === 'data.resourceIcon'">
          <span>
               <component :is="record.data.resourceIcon"/>
            </span>
        </template>
        <template v-if="column.dataIndex === 'data.resourceType'">
          <span>
            {{ getSelectLabel(resourceTypes, record.data.resourceType) }}
          </span>
        </template>
        <template v-if="column.dataIndex == 'data.resourceUrl'">
          {{ record.data.resourceUrl }}
        </template>
        <template v-if="column.dataIndex == 'data.describe'">
          {{ record.data.describe }}
        </template>
      </template>
    </a-table>
    <template #footer>
      <a-button type="primary" @click="okroot">根目录</a-button>
      <a-button @click="cancel">取消</a-button>
      <a-button type="primary" @click="ok">确定</a-button>
    </template>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, reactive, ref} from "vue";
import SysDictApi from "@/api/sys/SysDictApi";
import SysResourceApi from "@/api/sys/SysResourceApi";

export default defineComponent({
  name: 'MenuModal',
  data() {
    return {
      id: "",
      visible: false,
      loading: false,
      data: {},
      resourceList: [],
      columns: [
        {title: '资源名称', key: 'data.resourceName', dataIndex: 'data.resourceName', width: "250px", fixed: 'left'},
        {title: '资源ICON', key: 'data.resourceIcon', dataIndex: 'data.resourceIcon', width: "80px",},
        {title: '资源类型', key: 'data.resourceType', dataIndex: 'data.resourceType', width: "100px",},
        {title: '资源路径', key: 'data.resourceUrl', dataIndex: 'data.resourceUrl', width: "200px",},
        {title: '描述', key: 'data.describe', dataIndex: 'data.describe',},
      ],
    }
  },
  props: {
    title: {
      type: String,
      required: true
    },
    okCallback: {
      type: Function,
      default: (data: any, selectData: any) => {
        console.log(data, selectData)
      }
    },
  },
  methods: {
    okroot() {
      this.visible = false
      this.data.parentId = '-1'
      this.data.resources = ['-1']
      let selectData: any[] = []
      selectData.push(
          {
            id: '-1',
            resourceName: '根'
          }
      )
      this.okCallback(this.data, selectData)
    },
    ok() {
      this.visible = false
      this.data.parentId = this.selectedRowKeys[0]
      this.data.resources = this.selectedRowKeys
      let selectData: any[] = []
      let resources = this.getResourceAndChild(this.resourceList)
      this.selectedRowKeys.forEach((rowkey: any) => {
        let result = resources.filter((resource: any) => {
          if (resource.id == rowkey) {
            return resource
          }
        })
        if (result.length > 0) {
          selectData.push(result[0])
        }
      })
      console.log(selectData)
      this.okCallback(this.data, selectData)
    },
    getResourceAndChild(resourceList: any) {
      let resources: any[] = []
      resourceList.forEach((resource: any) => {
        if (resource.child == undefined) {
          resources.push(resource.data)
        } else if (resource.child.length > 0) {
          resources = resources.concat(this.getResourceAndChild(resource.child))
          resources.push(resource.data)
        }
      })
      return resources
    },
    getAllResource() {
      SysResourceApi.getAllResource().then(res => {
        this.resourceList = res.data.data
        this.loading = false
      })
    },
    cancel() {
      this.visible = false
    },
    open(data: any) {
      this.visible = true
      this.data = data
      this.id = data.id
      this.selectedRowKeys = data.resources

    },

  },
  created() {

  },
  mounted() {
    this.getAllResource()
  },
  setup() {
    let resourceTypes = ref([])
    onMounted(async () => {
      const res = await SysDictApi.getSelect('资源类型')
      resourceTypes.value = res.data.data
    });

    let selectedRowKeys = reactive({});
    const type = reactive({});

    const rowSelection = function rowSelection() {
      let selection = {
        type: type,
        selectedRowKeys: selectedRowKeys,
      }

      if (type == 'radio') {
        (selection as any).onSelect = (record: any, selected: any) => {
          if (selected) {
            selectedRowKeys = [record.id]
          } else {
            selectedRowKeys = []
          }
        }
      } else {
        (selection as any).onChange = (iselectedRowKeys: any) => {
          selectedRowKeys = iselectedRowKeys

        }
      }
      return selection
    }

    return {resourceTypes, selectedRowKeys, type, rowSelection}
  }
})

</script>

