import {AxiosResponse} from "axios"
import Qs from "qs";

const MD5 = require('md5.js')

const app = window.vm

const axios = app.config.globalProperties.$axios

const url = {
    list: "/sysUser/list",
    all: "/sysUser/all",
    get: "/sysUser/get",
    add: "/sysUser/add",
    del: "/sysUser/del",
    checkRoleName: "/sysUser/checkRoleName",
    getRoleByUserId: "/sysUser/getRoleByUserId",
    checkPassword: "/sysUser/checkPassword",
    changePassword: "/sysUser/changePassword",
    resetPassword: "/sysUser/resetPassword",
    checkUsername: "/sysUser/checkUsername",
    checkNickname: "/sysUser/checkNickname",

}


export default class SysUserApi {

    public static list(data: any, page?: any, sort?: any): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.list,
            params: Object.assign(data, page, sort)
        })
    }

    public static get(id:string): Promise<AxiosResponse> {
        console.log(id)
        return axios({
            method: 'get',
            url: url.get,
            params: {
                id: id
            }
        })
    }

    public static getAll(): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.all,
        })
    }

    public static getRoleByUserId(userId: string): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            params: {
                userId: userId
            },
            url: url.getRoleByUserId,
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

    public static changePassword(userId: string, password: string,oldPassword: string): Promise<AxiosResponse> {
        password = new MD5().update(password).digest('hex')
        oldPassword = new MD5().update(oldPassword).digest('hex')
        return axios({
            method: 'post',
            url: url.changePassword,
            data: Qs.stringify(({
                userId: userId,
                password: password,
                oldPassword: oldPassword,
            }))
        })
    }

    public static checkUseruame(username: string, originalValue?: string): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.checkUsername,
            params: {
                checkValue: username,
                originalValue: originalValue
            }
        })
    }

    public static checkNickname(username: string, originalValue?: string): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.checkNickname,
            params: {
                checkValue: username,
                originalValue: originalValue
            }
        })
    }

    public static checkPassword(userId: string, password: string,username?:string): Promise<AxiosResponse> {
        password = new MD5().update(password).digest('hex')
        return axios({
            method: 'get',
            url: url.checkPassword,
            params: {
                username: username,
                userId: userId,
                password: password
            }
        })
    }

    public static resetPassword(userId: string): Promise<AxiosResponse> {
        return axios({
            method: 'post',
            url: url.resetPassword,
            data: Qs.stringify({
                userId: userId,
            })
        })
    }
}
