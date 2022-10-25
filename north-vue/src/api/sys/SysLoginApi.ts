import {AxiosResponse} from "axios";
import HttpClient, {RespData} from "@/plugins/axios/HttpClient";
import Qs from "qs";

const MD5 = require('md5.js')

export type RegisterData = {
    username: string | undefined;
    password: string | undefined;
    nickname: string | undefined;
    phone: number | undefined;
    email: string | undefined;
}


export type LoginData = {
    username: string;
    password: string;
    redirect: string;
    hash: string;
    genId: string;
}



class SysLoginApi {

    readonly url = {
        register: "/sysLogin/register",
        pyToolRegister: "/smallTool/sys/register",
        pyChangePassword: "/smallTool/sys/changePassword",
        login: "/sysLogin/login",
        ssoLogin: "/sso-server/ssoLogin",
        logout: "/sysLogin/logout",
        gen: "/sysLogin/gen",
        check: "/sysLogin/check",
    }

    public register(registerData: RegisterData): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: "post",
            url: this.url.register,
            data: Qs.stringify(({
                username: registerData.username,
                password: registerData.password,
                nickname: registerData.nickname,
                phone: registerData.phone,
                email: registerData.email,
            }))
        })
    }

    public pyToolRegister(registerData: RegisterData): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: "post",
            url: this.url.pyToolRegister,
            data: Qs.stringify(({
                username: registerData.username,
                password: registerData.password,
                nickname: registerData.nickname,
                phone: registerData.phone,
                email: registerData.email,
            }))
        })
    }

    public login(loginData: LoginData): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: "post",
            url: this.url.login,
            data: Qs.stringify(({
                username: loginData.username,
                password: loginData.password,
                genId: loginData.genId,
            }))
        })
    }

    public ssoLogin(loginData: LoginData): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: "post",
            url: this.url.ssoLogin,
            data: Qs.stringify(({
                username: loginData.username,
                password: loginData.password,
                redirect: loginData.redirect,
                hash: loginData.hash,
            }))
        })
    }

    public logout(): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'post',
            url: this.url.logout,
        })
    }


    public pyChangePassword(key: string, username: string, password: string, oldPassword: string): Promise<AxiosResponse<RespData>> {
        console.log(password)
        password = new MD5().update(password).digest('hex')
        oldPassword = new MD5().update(oldPassword).digest('hex')
        console.log(password)
        return HttpClient.request({
            method: 'post',
            url: this.url.pyChangePassword,
            data: Qs.stringify(({
                key: key,
                username: username,
                password: password,
                oldPassword: oldPassword,
            }))
        })
    }

    public gen(): Promise<AxiosResponse> {
        return HttpClient.request({
            method: 'post',
            url: this.url.gen,
        })
    }

    public check(data: any): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'post',
            url: this.url.check,
            data: data
        })
    }
}

const api = new SysLoginApi()
export default api