<style lang="less">

</style>

<template>
  <a-modal :onCancel="cancel" :onOk="ok" :title="title" :visible="visible" :width="720">
    <a-table :childrenColumnName="'child'" :columns="columns" :data-source="resourceList" :defaultExpandAllRows="true"
             :loading="loading" :pagination="false" :rowKey="(record)=>record.id"
             :rowSelection="rowSelection" :scroll="{ x: 900, y: 500 }">
      <template #icon="{ text }">
          <span>
             <component :is="$icons[text]"/>
          </span>
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
import {Options, Vue} from "vue-class-component";
import SysResourceApi from "@/api/SysResourceApi";
import {AxiosResponse} from "axios";
import SysDictApi from "@/api/SysDictApi";

@Options({
  name: 'MenuModal',
  data() {
    let resourceTypes: never[] = [];
    SysDictApi.getSelect('资源类型').then((res: AxiosResponse) => {
      resourceTypes = res.data.data
    });
    return {
      id: "",
      visible: false,
      loading: false,
      data: {},
      resourceList: [],
      selectedRowKeys: [],
      columns: [
        {title: '资源名称', key: 'data.resourceName', dataIndex: 'data.resourceName', width: "250px", fixed: 'left'},
        {
          title: '资源ICON',
          key: 'data.resourceIcon',
          dataIndex: 'data.resourceIcon',
          slots: {customRender: "icon"},
          width: "80px",
        },
        {
          title: '资源类型', key: 'data.resourceType', dataIndex: 'data.resourceType', width: "100px",
          customRender: function (record: any) {
            for (let resourceType of resourceTypes) {
              let type = (<any>resourceType)
              if (type.value == record.record.data.resourceType) {
                return type.lable
              }
            }
            return record.record.data.resourceType == 1 ? "菜单" : "资源"
          }
        },
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
    type: {
      type: String,
      default: () => {
        return 'checkbox'
      }
    },
    okCallback: {
      type: Function,
      default: (data: any, selectData: any) => {
        console.log(data, selectData)
      }
    },
  },
  methods: {
    okroot(){
      this.visible = false
      this.data.parentId = '-1'
      this.data.resources = ['-1']
      let selectData: any[] = []
      selectData.push(
          {
            id : '-1',
            resourceName : '根'
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
        } else if (resource.child != undefined && resource.child.length > 0) {
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
  mounted() {
    this.getAllResource()
  },
})

export default class ParentIdModal extends Vue {
  selectedRowKeys: any;
  type: any;

  get rowSelection() {
    let selection = {
      type: this.type,
      selectedRowKeys: this.selectedRowKeys,
    }

    if (this.type == 'radio') {
      (selection as any).onSelect = (record: any, selected: any, selectedRows: any[], nativeEvent: any) => {
        if (selected) {
          this.selectedRowKeys = [record.id]
        } else {
          this.selectedRowKeys = []
        }
      }
    } else {
      (selection as any).onChange = (selectedRowKeys: any, selectedRows: any) => {
        this.selectedRowKeys = selectedRowKeys

      }
    }
    return selection
  }
}

</script>

