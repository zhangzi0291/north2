import {BaseCrulApi} from "@/api/BaseApi";
import {AxiosResponse} from "axios";
import HttpClient, {RespData} from "@/plugins/axios/HttpClient";
import Qs from "qs";

class SysUserOnlineApi extends BaseCrulApi {

    readonly url = {
        list: "/sysUser/onlineUserList",
        kickUser: "/sysUser/kickUser",
    }

    constructor() {
        super({
            list: "/sysUser/onlineUserList",
        });
    }

    public kickUser(id: string): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'post',
            url: this.url.kickUser,
            data: Qs.stringify({
                id: id
            })
        })
    }
}

const api = new SysUserOnlineApi()
export default api