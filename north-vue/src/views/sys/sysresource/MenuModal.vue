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
  </a-modal>
</template>

<script lang="ts">
import {Options, Vue} from "vue-class-component";
import SysResourceApi from "@/api/SysResourceApi";
import {AxiosResponse} from "axios";
import SysDictApi from "@/api/SysDictApi";

let sysApi = new SysResourceApi()
let dictApi = new SysDictApi();

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
        {title: '资源名称', key: 'data.resourceName', dataIndex: 'data.resourceName',width:"250px",fixed: 'left'},
        {title: '资源ICON', key: 'data.resourceIcon', dataIndex: 'data.resourceIcon', slots: {customRender: "icon"},width:"80px",},
        {
          title: '资源类型', key: 'data.resourceType', dataIndex: 'data.resourceType',width:"100px",
          customRender: function (record: any) {
            for (let resourceType  of resourceTypes) {
              let type = (<any>resourceType)
              if(type.value == record.record.data.resourceType){
                return type.lable
              }
            }
            return record.record.data.resourceType == 1 ? "菜单" : "资源"
          }
        },
        {title: '资源路径', key: 'data.resourceUrl', dataIndex: 'data.resourceUrl',width:"200px",},
        {title: '描述', key: 'data.describe', dataIndex: 'data.describe',},
      ],
    }
  },
  props: {
    title:{
      type:String,
      required:true
    },
    type:{
      type:String,
      default: () => {
        return 'checkbox'
      }
    },
    okCallback: {
      type: Function,
      default: (data: any) => {
        console.log(data)
      }
    },
  },
  methods: {
    ok() {
      this.visible = false
      this.data.parentId = this.selectedRowKeys[0]
      this.data.resources = this.selectedRowKeys
      this.okCallback(this.data)
    },
    getAllResource() {
      sysApi.getAllResource().then(res => {
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
      console.log(this.selectedRowKeys)

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

    if(this.type =='radio'){
      (selection as any).onSelect = (record: any, selected: any, selectedRows: any[], nativeEvent: any) => {
        if (selected) {
          this.selectedRowKeys = [record.id]
        } else {
          this.selectedRowKeys = []
        }
        console.log(this.selectedRowKeys)
      }
    }else{
      (selection as any).onChange = (selectedRowKeys: any, selectedRows: any) => {
        this.selectedRowKeys = selectedRowKeys
        console.log(this.selectedRowKeys)

      }
    }
    return selection
  }
}

</script>

