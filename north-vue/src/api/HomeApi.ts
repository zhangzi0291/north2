import {AxiosResponse} from "axios"

const app = window.vm

const axios = app.config.globalProperties.$axios

const url = {
    getTotalUser: "/home/getTotalUser",
    getOnlineUser: "/home/getOnlineUser",
    getTodayUser: "/home/getTodayUser",
    getHardwareInfo: "/home/getHardwareInfo",

}


export default class HomeApi {

    public static getTotalUser(): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.getTotalUser,
        })
    }

    public static getOnlineUser(): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.getOnlineUser,
        })
    }

    public static getTodayUser(): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.getTodayUser,
        })
    }

    public static getHardwareInfo(): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.getHardwareInfo,
        })
    }
}
