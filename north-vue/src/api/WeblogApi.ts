import {AxiosResponse} from "axios"
import HttpClient, {RespData} from "@/plugins/axios/HttpClient";
import {BaseCrulApi} from "@/api/BaseApi";

const url = {
    list: "/weblog/list",
    all: "/weblog/all",
    get: "/weblog/get",
    add: "/weblog/addWeblog",
    del: "/weblog/del",
    publicList: "/weblog/publicList",

}


class WeblogApi extends BaseCrulApi {

    constructor() {
        super({
            list: "/weblog/list",
            all: "/weblog/all",
            get: "/weblog/get",
            add: "/weblog/addWeblog",
            del: "/weblog/del",
        });
    }

    public publicList(data: any, page?: any, sort?: any): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'get',
            url: url.publicList,
            params: Object.assign(data, page, sort)
        })
    }

}

const api = new WeblogApi()
export default api