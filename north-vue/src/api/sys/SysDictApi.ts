import baseApi, {BaseCrulApi} from "@/api/BaseApi";
import {AxiosResponse} from "axios";
import HttpClient, {RespData} from "@/plugins/axios/HttpClient";

class SysDictApi extends BaseCrulApi {

    readonly url = {
        list: "/sysDict/list",
        all: "/sysDict/all",
        add: "/sysDict/add",
        del: "/sysDict/del",
    }

    constructor() {
        super({
            list: "/sysDict/list",
            all: "/sysDict/all",
            add: "/sysDict/add",
            del: "/sysDict/del",
        });
    }

    public list(data: any, page?: any, sort?: any): Promise<AxiosResponse<RespData>> {
        return baseApi.list(this.url.list, data, page, sort)
    }

    public getAll(): Promise<AxiosResponse<RespData>> {
        return baseApi.getAll(this.url.all)
    }


    public del(ids: Array<string>): Promise<AxiosResponse<RespData>> {
        return baseApi.del(this.url.del, ids)
    }

    public add(data: any): Promise<AxiosResponse<RespData>> {
        return baseApi.add(this.url.add, data)
    }

    public getSelect(dictName: string): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: "get",
            url: "/sysDict/getSelectFieldList",
            params: {
                dictName: dictName
            }
        })
    }

}

const api = new SysDictApi()
export default api