import {AxiosResponse} from "axios"
import Qs from "qs";

const app = window.vm

const axios = app.config.globalProperties.$axios

const url = {
    list: "/pytoolApp/homePageInfoList",
    add: "/pytoolApp/uploadHomePageInfo",
    del: "/pytoolApp/delHomePageInfo",

}


export default class PytoolAppApi {

    public static list(data: any, page?: any, sort?: any): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.list,
            params: Object.assign(data, page, sort)
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

}
