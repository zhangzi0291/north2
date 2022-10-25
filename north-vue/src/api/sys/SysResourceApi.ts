import baseApi, {BaseCrulApi} from "@/api/BaseApi";
import {AxiosResponse} from "axios";
import HttpClient, {RespData} from "@/plugins/axios/HttpClient";

class SysResourceApi extends BaseCrulApi {

    readonly url = {
        getMenu: "/sysResource/getMenuTree?pid=-1",
        getAllResource: "/sysResource/getResourceTree?pid=-1",
        checkResourceName: "/sysResource/checkResourceName",
    }

    constructor() {
        super({
            get: "/sysResource/get",
            add: "/sysResource/add",
            del: "/sysResource/del",
        });
    }

    public getMenu(): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'get',
            url: this.url.getMenu,
        })
    }

    public getAllResource(): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'get',
            url: this.url.getAllResource,
        })
    }

    public getResource(id: string): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'get',
            url: this.baseUrl.get,
            params: {
                id: id
            }
        })
    }

    public checkResourceName(resourceName: string, originalValue?: string): Promise<AxiosResponse<RespData>> {
        return baseApi.checkValue(this.url.checkResourceName, resourceName, originalValue)
    }
}

const api = new SysResourceApi()
export default api