import {AxiosResponse} from "axios"
import Qs from "qs";
import HttpClient, {RespData} from "@/plugins/axios/HttpClient";
import {BaseCrulApi} from "@/api/BaseApi";

const url = {

    checkSoftName: "/pytoolApp/checkSoftName",
    homePageList: "/pytoolApp/homePageInfoList",
    homePageAdd: "/pytoolApp/uploadHomePageInfo",
    homePageDel: "/pytoolApp/delHomePageInfo",
    historyList: "/pytoolApp/appHistoryList",
}


class PytoolAppApi extends BaseCrulApi {

    constructor() {
        super({
            list: "/pytoolApp/appList",
            add: "/pytoolApp/addWithFile",
            del: "/pytoolApp/del",
        });
    }

    public gethistoryList(softName: string, page?: any): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'get',
            url: url.historyList,
            params: Object.assign({
                softName: softName
            }, page)
        })
    }

    public checkSoftName(checkValue: string, originalValue?: string): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'get',
            url: url.checkSoftName,
            params: {
                checkValue: checkValue,
                originalValue: originalValue
            }
        })
    }

    public homePageList(data: any, page?: any, sort?: any): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'get',
            url: url.homePageList,
            params: Object.assign(data, page, sort)
        })
    }

    public homePageDel(ids: Array<string>): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'post',
            url: url.homePageDel,
            data: Qs.stringify({
                ids: ids
            }, {indices: false})
        })
    }

    public homePageAdd(data: any): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'post',
            url: url.homePageAdd,
            data: Qs.stringify((data))
        })
    }
}

const api = new PytoolAppApi()
export default api