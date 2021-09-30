import {AxiosResponse} from "axios"
import Qs from "qs";

const app = window.vm

const axios = app.config.globalProperties.$axios

const url = {
    list: "/sysRole/list",
    all: "/sysRole/all",
    add: "/sysRole/add",
    del: "/sysRole/del",
    checkRoleName: "/sysRole/checkRoleName",
    getResourceByRoleId: "/sysRole/getResourceByRoleId",

}


export default class SysRoleApi {

    public static list(data: any, page?: any, sort?: any): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.list,
            params: Object.assign(data, page, sort)
        })
    }

    public static getAll(): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.all,
        })
    }

    public static getResourceByRoleId(roleId: string): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            params: {
                roleId: roleId
            },
            url: url.getResourceByRoleId,
        })
    }

    public static del(ids: Array<string>): Promise<AxiosResponse> {
        return axios({
            method: 'post',
            url: url.del,
            data: Qs.stringify({
                ids: ids
            }, {indices: false})
        })
    }

    public static add(data: any): Promise<AxiosResponse> {
        return axios({
            method: 'post',
            url: url.add,
            data: Qs.stringify((data))
        })
    }

    public static checkRoleName(roleName: string, originalValue?: string): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.checkRoleName,
            params: {
                checkValue: roleName,
                originalValue: originalValue
            }
        })
    }
}
