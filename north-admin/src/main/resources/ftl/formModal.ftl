<style scoped lang="less">
    /deep/ .title {
        font-size: 20px;
        font-weight: 900;
    }

    /deep/ .ivu-form-item-content {
        word-wrap: break-word;
        word-break: break-all;
    }
</style>
<template>
    <Modal v-model="show" scrollable width="65" @on-ok='ok'>
        <Row class="title">新增{{title}}</Row>
        <Row>
            <Form :model="data" inline>
                <#list tableInfoList as tableInfo>
                <#if tableInfo_index%2 ==0>
                <Row>
                    </#if>
                    <Col span="12">
                    <FormItem label="${tableInfo.comment!}：" :label-width="labelwidth">
                        <Input v-model="data.${tableInfo.propertyName}" placeholder="" clearable/>
                    </FormItem>
                    </Col>
                    <#if tableInfo_index%2 ==1>
                </Row>
                </#if>
                <#if !tableInfo_has_next>
        </Row>
        </#if>
        </#list>

        </Form>
        </Row>
    </Modal>
</template>
<script>
    export default {
        name: "sysLogForm",
        data() {
            return {
                url: "",
                labelwidth: 120,
                id: "",
                data: {},
                show: false,
            }

        },
        props: {
            //标题
            title: {
                type: String,
                default: ""
            },
            //查询单条记录接口的url
            getUrl: {
                type: String,
                default: ""
            },
            //新增接口的url
            addUrl: {
                type: String,
                default: ""
            },
            //编辑接口的url
            editUrl: {
                type: String,
                default: ""
            },
        },
        filters: {},
        watch: {},
        methods: {
            //提交
            ok: function () {
                let $this = this;
                //可以做校验
                //   if (this.$refs.editModal.validateForm()) {
                //     $this.addshow = false;
                //     setTimeout(function() {
                //       $this.addshow = true;
                //     }, 1);
                //     return;
                //   }
                this.$ajax({
                    method: "post",
                    url: this.url,
                    data: this.data
                }).then(function (res) {
                    $this.successModal("操作成功");
                    //刷新表格
                    $this.$parent.$refs.table.searchData();
                });
            },
            //弹出或关闭模态窗
            changeShow(id) {
                this.id = id;
                console.log(this.id)
                this.show = !this.show
                if (!!this.id) {
                    this.$ajax({
                        method: "post",
                        url: this.getUrl,
                        data: {
                            id: this.id
                        }
                    }).then(res => {
                        this.data = res.data.data
                    });
                    this.url = this.editUrl
                } else {
                    this.url = this.addUrl
                    this.data = {}
                }

            },
        },
        mounted() {
        }
    };
</script>
