import {AxiosResponse} from "axios"
import Qs from "qs";

const MD5 = require('md5.js')

const app = window.vm

const axios = app.config.globalProperties.$axios

const url = {
    register: "/sysLogin/register",
    pyToolRegister: "/smallTool/sys/register",
    pyChangePassword: "/smallTool/sys/changePassword",
    login: "/sysLogin/login",
    ssoLogin:"/sso-server/doLogin",
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
    redirect: string | undefined;
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

    public static pyToolRegister(registerData:RegisterData): Promise<AxiosResponse> {
        return axios({
            method: "post",
            url: url.pyToolRegister,
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

    public static ssoLogin(loginData:LoginData): Promise<AxiosResponse> {
        return axios({
            method: "post",
            url: url.ssoLogin,
            data: Qs.stringify(({
                username: loginData.username,
                password: loginData.password,
                redirect: loginData.redirect,
            }))
        })
    }

    public static logout(): Promise<AxiosResponse> {
        return axios({
            method: 'post',
            url: url.logout,
        })
    }


    public static pyChangePassword(key:string,username: string, password: string,oldPassword: string): Promise<AxiosResponse> {
        console.log(password)
        password = new MD5().update(password).digest('hex')
        oldPassword = new MD5().update(oldPassword).digest('hex')
        console.log(password)
        return axios({
            method: 'post',
            url: url.pyChangePassword,
            data: Qs.stringify(({
                key: key,
                username: username,
                password: password,
                oldPassword: oldPassword,
            }))
        })
    }
}
