import baseApi, {BaseCrulApi} from "@/api/BaseApi";
import {AxiosResponse} from "axios";
import HttpClient, {RespData} from "@/plugins/axios/HttpClient";
import Qs from "qs";

class EtAppInfoApi extends BaseCrulApi {

    readonly url = {
        getRole:"/etRole/getRole",
        distRole:"/etRole/distRole",
    }

    constructor() {
        super({
            list: "/etRole/list",
            all: "/etRole/all",
            add: "/etRole/add",
            del: "/etRole/del",
        });
    }

    public getRoleByUserId(userId: string): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'get',
            url: this.url.getRole,
            params: {
                userId: userId
            }
        })
    }

    public distRole(userId: string,roleIds): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'post',
            url: this.url.distRole,
            data: Qs.stringify({
                userId: userId,
                roleIds: roleIds,
            }, {indices: false})
        })
    }
}

const api = new EtAppInfoApi()
export default api
