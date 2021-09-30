import {reactive, ref} from "vue";
import SysUserApi from "@/api/SysUserApi";

let loading = ref(false)
let page = reactive({ current:1,total:0 })
let sort = reactive({ field:null,order:null})
let search = reactive({ })
let data = ref([])

const load = function (param?:any) {
    loading.value = true
    if(!!param && !!param.current ){
        page.current = param.current
    }
    SysUserApi.list(search,page,sort).then(res => {
        data.value.length=0
        data.value = data.value.concat(res.data.data.records)
        page.current = res.data.data.current
        page.total = res.data.data.total
        loading.value = false
    })
}
const tableChange = function (pageParam:any, filters:any, sorter:any){
    page.current = pageParam.current
    page.total = pageParam.total
    sort.field = sorter.field
    sort.order = sorter.order
    load()
}

