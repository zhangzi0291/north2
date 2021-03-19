import {AxiosResponse} from "axios"
import Qs from "qs";

const app = window.vm

const axios = app.config.globalProperties.$axios

const url = {
    list: "/sysLog/list",
    all: "/sysLog/all",
    del: "/sysLog/del",

}


export default class SysLogApi {

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

}
