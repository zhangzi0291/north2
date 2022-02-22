import {AxiosResponse} from "axios"
import Qs from "qs";

const app = window.vm

const axios = app.config.globalProperties.$axios

const url = {
    list: "/weblog/list",
    publicList: "/weblog/publicList",
    all: "/weblog/all",
    get: "/weblog/get",
    add: "/weblog/addWeblog",
    del: "/weblog/del",

}


export default class WeblogApi {

    public static list(data: any, page?: any, sort?: any): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.list,
            params: Object.assign(data, page, sort)
        })
    }

    public static publicList(data: any, page?: any, sort?: any): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.publicList,
            params: Object.assign(data, page, sort)
        })
    }

    public static get(id: string): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.get,
            params: {
                id: id
            }
        })
    }

    public static getAll(): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.all,
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

    public static add(data: any): Promise<AxiosResponse> {
        return axios({
            method: 'post',
            url: url.add,
            data: data
        })
    }


}
