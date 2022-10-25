import {AxiosResponse} from "axios";
import HttpClient, {RespData} from "@/plugins/axios/HttpClient";
import Qs from "qs";

export class BaseCrulApi {
    baseUrl = {
        list: "/list",
        all: "/all",
        get: "/get",
        add: "/add",
        del: "/del",
    }

    constructor(url) {
        this.baseUrl = url
    }

    public list(data: any, page?: any, sort?: any): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'get',
            url: this.baseUrl.list,
            params: Object.assign(data, page, sort)
        })
    }

    public getAll(): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'get',
            url: this.baseUrl.all,
        })
    }


    public del(ids: Array<string>): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'post',
            url: this.baseUrl.del,
            data: Qs.stringify({
                ids: ids
            }, {indices: false})
        })
    }

    public add(data: any): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'post',
            url: this.baseUrl.add,
            data: Qs.stringify((data))
        })
    }

    public get(id: string): Promise<AxiosResponse<RespData>> {
        console.log(id)
        return HttpClient.request({
            method: 'get',
            url: this.baseUrl.get,
            params: {
                id: id
            }
        })
    }
}

class BaseApi {

    public list(url: string, data: any, page?: any, sort?: any): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'get',
            url: url,
            params: Object.assign(data, page, sort)
        })
    }

    public getAll(url: string): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'get',
            url: url,
        })
    }


    public del(url: string, ids: Array<string>): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'post',
            url: url,
            data: Qs.stringify({
                ids: ids
            }, {indices: false})
        })
    }

    public add(url: string, data: any): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'post',
            url: url,
            data: Qs.stringify((data))
        })
    }

    public get(url: string, id: string): Promise<AxiosResponse<RespData>> {
        console.log(id)
        return HttpClient.request({
            method: 'get',
            url: url,
            params: {
                id: id
            }
        })
    }

    public checkValue(url: string, newValue: string, originalValue?: string): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'get',
            url: url,
            params: {
                checkValue: newValue,
                originalValue: originalValue
            }
        })
    }

    public setUserByStorage(user: any) {
        window.localStorage.setItem("user", JSON.stringify(user))
    }

    public setTokenByStorage(token: string) {
        window.localStorage.setItem("token", token)
    }

    public setPermissionsByStorage(permissions: any) {
        window.localStorage.setItem("permissions", JSON.stringify(permissions))

    }

    public setRolesByStorage(roles: any) {
        window.localStorage.setItem("roles", JSON.stringify(roles))
    }

    public getUserByStorage() {
        let user = window.localStorage.getItem("user")
        if (user != null) {
            return JSON.parse(user)
        }
        return {};
    }

    public getTokenByStorage() {
        return window.localStorage.getItem("token")
    }

    public getPermissionsByStorage() {
        let permissions = window.localStorage.getItem("permissions")
        if (permissions != null) {
            return JSON.parse(permissions)
        }
        return [];
    }

    public getRolesByStorage() {
        let roles = window.localStorage.getItem("roles")
        if (roles != null) {
            return JSON.parse(roles)
        }
        return []
    }

    public setWsIsClose(isClose: boolean) {
        window.localStorage.setItem("wsIsClose", JSON.stringify(isClose))
    }

    public getWsIsClose() {
        let isClose = window.localStorage.getItem("wsIsClose")
        if (isClose != null) {
            return !!JSON.parse(isClose)
        }
        return false;
    }

}

const api = new BaseApi()
export default api