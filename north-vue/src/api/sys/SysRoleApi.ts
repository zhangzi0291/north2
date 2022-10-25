import baseApi, {BaseCrulApi} from "@/api/BaseApi";
import {AxiosResponse} from "axios";
import HttpClient, {RespData} from "@/plugins/axios/HttpClient";

class SysRoleApi extends BaseCrulApi {

    readonly url = {
        list: "/sysRole/list",
        all: "/sysRole/all",
        add: "/sysRole/add",
        del: "/sysRole/del",
        checkRoleName: "/sysRole/checkRoleName",
        getResourceByRoleId: "/sysRole/getResourceByRoleId",

    }

    constructor() {
        super({
            list: "/sysRole/list",
            all: "/sysRole/all",
            add: "/sysRole/add",
            del: "/sysRole/del",
        });
    }

    public getResourceByRoleId(roleId: string): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'get',
            params: {
                roleId: roleId
            },
            url: this.url.getResourceByRoleId,
        })
    }

    public checkRoleName(roleName: string, originalValue?: string): Promise<AxiosResponse<RespData>> {
        return baseApi.checkValue(this.url.checkRoleName, roleName, originalValue)
    }
}

const api = new SysRoleApi()
export default api