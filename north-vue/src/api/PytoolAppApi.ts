import {AxiosResponse} from "axios"
import Qs from "qs";

const app = window.vm

const axios = app.config.globalProperties.$axios

const url = {
    list: "/pytoolApp/appList",
    historyList: "/pytoolApp/appHistoryList",
    add: "/pytoolApp/addWithFile",
    del: "/pytoolApp/del",
    checkSoftName: "/pytoolApp/checkSoftName"

}


export default class PytoolAppApi {

    public static list(data: any, page?: any, sort?: any): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.list,
            params: Object.assign(data, page, sort)
        })
    }

    public static gethistoryList(softName:string,page?: any): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.historyList,
            params: Object.assign({
                softName:softName
            }, page)
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

    public static checkSoftName(checkValue: string, originalValue?: string): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.checkSoftName,
            params: {
                checkValue: checkValue,
                originalValue: originalValue
            }
        })
    }
}
