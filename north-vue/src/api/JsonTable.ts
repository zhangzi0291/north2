import {AxiosResponse} from "axios"
import Qs from "qs";

const MD5 = require('md5.js')

const app = window.vm

const axios = app.config.globalProperties.$axios

const url = {
    list: "/jsonTable/list",
    get: "/jsonTable/get",
    del: "/jsonTable/del",
    addMate: "/jsonTableMate/addMateList",
    getMate: "/jsonTableMate/getMateList",
    getTableValue: "/jsonTableValue/getTableValue",
    addValue: "/jsonTableValue/add",
    delValue: "/jsonTableValue/del",
    delAllValue: "/jsonTableValue/delAll",

}


export default class JsonTableAPi {

    public static list(data: any, page?: any, sort?: any): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.list,
            params: Object.assign(data, page, sort)
        })
    }

    public static get(id: string): Promise<AxiosResponse> {
        console.log(id)
        return axios({
            method: 'get',
            url: url.get,
            params: {
                id: id
            }
        })
    }

    public static del(ids: Array<string>): Promise<AxiosResponse> {
        return axios({
            method: 'post',
            url: url.del,
            data: Qs.stringify({
                ids: ids
            }, {indices: false})
        })
    }

    public static addMate(tableId: any, data: any): Promise<AxiosResponse> {
        const mateList: any = []
        data.forEach((d: any) => {
            mateList.push(JSON.stringify(d))
        })
        return axios({
            method: 'post',
            url: url.addMate,
            data: {
                tableId: tableId,
                mateList: JSON.stringify(data)
            }
        })
    }

    public static getMate(tableId: any): Promise<AxiosResponse> {
        return axios({
            method: 'post',
            url: url.getMate,
            data: Qs.stringify({
                tableId: tableId
            })
        })
    }

    public static valueList(tableId: string, data: any, page?: any, sort?: any): Promise<AxiosResponse> {
        data.tableId = tableId
        return axios({
            method: 'get',
            url: url.getTableValue,
            params: Object.assign(data, page, sort)
        })
    }

    public static addValue(tableId: string, data: any): Promise<AxiosResponse> {
        data.tableId = tableId
        return axios({
            method: 'get',
            url: url.addValue,
            params: Qs.stringify({
                tableId: tableId,
                jsonValue: JSON.stringify(data)
            })
        })
    }

    public static delValue(ids: Array<string>): Promise<AxiosResponse> {
        return axios({
            method: 'post',
            url: url.delValue,
            data: Qs.stringify({
                ids: ids
            }, {indices: false})
        })
    }

    public static delAllValue(tableId: string): Promise<AxiosResponse> {
        return axios({
            method: 'post',
            url: url.delAllValue,
            data: Qs.stringify({
                tableId: tableId
            })
        })
    }
}
