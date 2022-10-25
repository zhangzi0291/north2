import baseApi, {BaseCrulApi} from "@/api/BaseApi";
import {AxiosResponse} from "axios";
import HttpClient, {RespData} from "@/plugins/axios/HttpClient";
import Qs from "qs";

class EtAppInfoApi extends BaseCrulApi {

    readonly url = {
        getApp:"/etAppInfo/getApp",
        distApp:"/etAppInfo/distApp"
    }

    constructor() {
        super({
            list: "/etAppInfo/list",
            all: "/etAppInfo/all",
            add: "/etAppInfo/add",
            del: "/etAppInfo/del",
        });
    }

    public getAppByRoleId(roleId: string): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'get',
            url: this.url.getApp,
            params: {
                roleId: roleId
            }
        })
    }

    public distApp(roleId: string,appIds): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'post',
            url: this.url.distApp,
            data: Qs.stringify({
                roleId: roleId,
                appIds: appIds,
            }, {indices: false})
        })
    }
}

const api = new EtAppInfoApi()
export default api
