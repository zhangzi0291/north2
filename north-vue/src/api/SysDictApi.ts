import {AxiosResponse} from "axios"
import Qs from "qs";

const app = window.vm

const axios = app.config.globalProperties.$axios

const url = {
    list: "/sysDict/list",
    all: "/sysDict/all",
    add: "/sysDict/add",
    del: "/sysDict/del",

}


export default class SysDictApi {

    public list(data: any, page?: any, sort?: any): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.list,
            params: Object.assign(data, page, sort)
        })
    }

    public getAll(): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.all,
        })
    }


    public del(ids: Array<string>): Promise<AxiosResponse> {
        return axios({
            method: 'post',
            url: url.del,
            data: Qs.stringify({
                ids: ids
            }, {indices: false})
        })
    }

    public add(data: any): Promise<AxiosResponse> {
        return axios({
            method: 'post',
            url: url.add,
            data: Qs.stringify((data))
        })
    }

    public static getSelect(dictName: string): Promise<AxiosResponse> {
        return axios({
            method: "get",
            url: "/sysDict/getSelectFieldList",
            params: {
                dictName: dictName
            }
        })
    }

}
