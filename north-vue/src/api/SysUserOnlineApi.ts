import {AxiosResponse} from "axios"
import Qs from "qs";

const app = window.vm

const axios = app.config.globalProperties.$axios

const url = {
    list: "/sysUser/onlineUserList",
    kickUser: "/sysUser/kickUser",

}


export default class SysUserOnlineApi {

    public static list(data: any, page?: any, sort?: any): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.list,
            params: Object.assign(data, page, sort)
        })
    }

    public static kickUser(id: string): Promise<AxiosResponse> {
        return axios({
            method: 'post',
            url: url.kickUser,
            data: Qs.stringify({
                id: id
            })
        })
    }


}
