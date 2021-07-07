import {AxiosResponse} from "axios"
import Qs from "qs";

const MD5 = require('md5.js')

const app = window.vm

const axios = app.config.globalProperties.$axios

const url = {
    register: "/sysLogin/register",
    login: "/sysLogin/login",
    logout: "/sysLogin/logout",

}

export interface RegisterData{
    username: string | undefined;
    password: string | undefined;
    nickname: string | undefined;
    phone: number | undefined;
    email: string | undefined;
}


export interface LoginData{
    username: string | undefined;
    password: string | undefined;
}

export default class SysLoginApi {

    public static register(registerData:RegisterData): Promise<AxiosResponse> {
        return axios({
            method: "post",
            url: url.register,
            data: Qs.stringify(({
                username: registerData.username,
                password: registerData.password,
                nickname: registerData.nickname,
                phone: registerData.phone,
                email: registerData.email,
            }))
        })
    }

    public static login(loginData:LoginData): Promise<AxiosResponse> {
        return axios({
            method: "post",
            url: url.login,
            data: Qs.stringify(({
                username: loginData.username,
                password: loginData.password,
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
