import {AxiosResponse} from "axios"
import HttpClient, {RespData} from "@/plugins/axios/HttpClient";

const url = {
    getTotalUser: "/home/getTotalUser",
    getOnlineUser: "/home/getOnlineUser",
    getTodayUser: "/home/getTodayUser",
    getHardwareInfo: "/home/getHardwareInfo",

}


class HomeApi {

    public getTotalUser(): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'get',
            url: url.getTotalUser,
        })
    }

    public getOnlineUser(): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'get',
            url: url.getOnlineUser,
        })
    }

    public getTodayUser(): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'get',
            url: url.getTodayUser,
        })
    }

    public getHardwareInfo(): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'get',
            url: url.getHardwareInfo,
        })
    }
}

const homeApi = new HomeApi()

export default homeApi