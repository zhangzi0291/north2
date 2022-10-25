import baseApi, {BaseCrulApi} from "@/api/BaseApi";
import {AxiosResponse} from "axios";
import {RespData} from "@/plugins/axios/HttpClient";

class SysAreaApi extends BaseCrulApi {

    readonly url = {
        list: "/sysArea/list",
        all: "/sysArea/all",
        add: "/sysArea/add",
        del: "/sysArea/del",
    }

    constructor() {
        super({
            list: "/sysArea/list",
            all: "/sysArea/all",
            add: "/sysArea/add",
            del: "/sysArea/del",
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

}

const api = new SysAreaApi()
export default api
