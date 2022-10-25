import baseApi, {BaseCrulApi} from "@/api/BaseApi";
import {AxiosResponse} from "axios";
import HttpClient, {RespData} from "@/plugins/axios/HttpClient";
import Qs from "qs";
const MD5 = require('md5.js')

class SysUserApi extends BaseCrulApi {

    readonly url = {
        getRoleByUserId: "/sysUser/getRoleByUserId",
        checkPassword: "/sysUser/checkPassword",
        changePassword: "/sysUser/changePassword",
        resetPassword: "/sysUser/resetPassword",
        changeUserStatus: "/sysUser/changeUserStatus",
        checkUsername: "/sysUser/checkUsername",
        checkNickname: "/sysUser/checkNickname",
        editWithRole: "/sysUser/editWithRole",
    }

    constructor() {
        super({
            list: "/sysUser/list",
            all: "/sysUser/all",
            get: "/sysUser/get",
            add: "/sysUser/add",
            del: "/sysUser/del",
        });
    }

    public getRoleByUserId(userId: string): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'get',
            params: {
                userId: userId
            },
            url: this.url.getRoleByUserId,
        })
    }

    public editWithResource(data: any): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: "post",
            url: this.url.editWithRole,
            data: Qs.stringify((data), {indices: false})
        })
    }

    public changePassword(userId: string, password: string, oldPassword: string): Promise<AxiosResponse<RespData>> {
        password = new MD5().update(password).digest('hex')
        oldPassword = new MD5().update(oldPassword).digest('hex')
        return HttpClient.request({
            method: 'post',
            url: this.url.changePassword,
            data: Qs.stringify(({
                userId: userId,
                password: password,
                oldPassword: oldPassword,
            }))
        })
    }

    public checkUseruame(username: string, originalValue?: string): Promise<AxiosResponse<RespData>> {
        return baseApi.checkValue(this.url.checkUsername, username, originalValue)
    }

    public checkNickname(username: string, originalValue?: string): Promise<AxiosResponse<RespData>> {
        return baseApi.checkValue(this.url.checkNickname, username, originalValue)
    }

    public checkPassword(userId: string, password: string, username?: string): Promise<AxiosResponse<RespData>> {
        password = new MD5().update(password).digest('hex')
        return HttpClient.request({
            method: 'get',
            url: this.url.checkPassword,
            params: {
                username: username,
                userId: userId,
                password: password
            }
        })
    }

    public resetPassword(userId: string): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'post',
            url: this.url.resetPassword,
            data: Qs.stringify({
                userId: userId,
            })
        })
    }

    public changeUserStatus(id: string): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'post',
            url: this.url.changeUserStatus,
            data: Qs.stringify({
                id: id,
            })
        })
    }
}

const api = new SysUserApi()
export default api