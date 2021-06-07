import {AxiosResponse} from "axios"
import Qs from "qs";

const MD5 = require('md5.js')

const app = window.vm

const axios = app.config.globalProperties.$axios

const url = {
    login: "/sysLogin/login",
    logout: "/sysLogin/logout",

}


export default class SysLoginApi {

    public static login(username: string, password: string): Promise<AxiosResponse> {
        return axios({
            method: "post",
            url: url.login,
            data: Qs.stringify(({
                username: username,
                password: password,
            }))
        })
    }

    public static logout(): Promise<AxiosResponse> {
        return axios({
            method: 'post',
            url: url.logout,
        })
    }

}
