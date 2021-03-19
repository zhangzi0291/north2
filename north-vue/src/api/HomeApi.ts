import {AxiosResponse} from "axios"
import Qs from "qs";

const app = window.vm

const axios = app.config.globalProperties.$axios

const url = {
    getTotalUser: "/home/getTotalUser",
    getOnlineUser: "/home/getOnlineUser",
    getTodayUser: "/home/getTodayUser",

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

}
