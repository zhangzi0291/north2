<style lang="less">

</style>

<template>
  <a-modal :onCancel="cancel" :onOk="ok" :title="'角色分配'" :visible="visible" :width="720">
    <a-table :childrenColumnName="'child'" :columns="columns" :data-source="roleList" :defaultExpandAllRows="true"
             :loading="loading" :pagination="false" :rowKey="(record)=>record.id"
             :rowSelection="rowSelection">

    </a-table>
  </a-modal>
</template>

<script lang="ts">
import {Options, Vue} from "vue-class-component";
import SysRoleApi from '@/api/SysRoleApi';

@Options({
  name: 'MenuModal',
  data() {
    return {
      id: "",
      visible: false,
      loading: false,
      data: {},
      roleList: [],
      selectedRowKeys: [],
      columns: [
        {title: '角色名', key: 'roleName', dataIndex: 'roleName',},
        {title: '角色描述', key: 'describe', dataIndex: 'describe',},
      ],
    }
  },
  props: {
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
      console.log(this.selectedRowKeys)
      if (this.selectedRowKeys.length != 0) {
        this.data.roleIds = this.selectedRowKeys
      } else {
        this.data.roleIds = null
      }
      this.okCallback(this.data)
    },
    getAllResource() {
      SysRoleApi.getAll().then(res => {
        this.roleList = res.data.data
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
      this.selectedRowKeys = data.roleIds
    },

  },
  mounted() {
    this.getAllResource()
  },
})

export default class RoleResourceModal extends Vue {
  selectedRowKeys: any;

  get rowSelection() {
    return {
      selectedRowKeys: this.selectedRowKeys,
      //表格默认选中
      onChange: (selectedRowKeys: any, selectedRows: any) => {
        console.log(selectedRowKeys)
        this.selectedRowKeys = selectedRowKeys
      },
    }
  }
}

</script>

