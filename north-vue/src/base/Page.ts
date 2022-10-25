import {reactive, ref} from "vue";
import {BaseCrulApi} from "@/api/BaseApi";

export function PageInfo(pageFunction: BaseCrulApi) {
    //表格加载状态
    let loading = ref(false)
    //分页
    let page = reactive({current: 1, total: 0})
    //排序
    let sort = reactive({field: null, order: null})
    //查询数据
    let search = reactive({})
    //表格数据
    let data = ref([])

    const load = function (param?: any) {
        loading.value = true
        if (param && param.current) {
            page.current = param.current
        }
        pageFunction.list(search, page, sort).then(res => {
            data.value.length = 0
            data.value = data.value.concat(res.data.data.records)
            page.current = res.data.data.current
            page.total = res.data.data.total
            loading.value = false
        })
    }
    const tableChange = function (pageParam: any, filters: any, sorter: any) {
        page.current = pageParam.current
        page.total = pageParam.total
        sort.field = sorter.field
        sort.order = sorter.order
        load()
    }

    const tablePageOption = {loading, data, page, search, load, tableChange}

    return tablePageOption
}


