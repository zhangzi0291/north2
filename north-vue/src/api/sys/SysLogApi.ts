import baseApi, {BaseCrulApi} from "@/api/BaseApi";
import {AxiosResponse} from "axios";
import {RespData} from "@/plugins/axios/HttpClient";

class SysLogApi extends BaseCrulApi {

    readonly url = {
        list: "/sysLog/list",
        all: "/sysLog/all",
        del: "/sysLog/del",
    }

    constructor() {
        super({
            list: "/sysLog/list",
            all: "/sysLog/all",
            del: "/sysLog/del",
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
}

const api = new SysLogApi();
export default api