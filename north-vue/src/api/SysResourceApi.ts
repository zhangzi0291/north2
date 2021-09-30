import {AxiosResponse} from "axios"
import Qs from "qs";

const app = window.vm

const axios = app.config.globalProperties.$axios

const url = {
    get: "/sysResource/get",
    getMenu: "/sysResource/getMenuTree?pid=-1",
    getAllResource: "/sysResource/getResourceTree?pid=-1",
    addResource: "/sysResource/add",
    delResource: "/sysResource/del",
    checkResourceName: "/sysResource/checkResourceName",

}


export default class SysResourceApi {

    public static getMenu(): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.getMenu,
        })
    }

    public static getAllResource(): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.getAllResource,
        })
    }

    public static getResource(id: string): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.get,
            params: {
                id: id
            }
        })
    }

    public static del(ids: Array<string>): Promise<AxiosResponse> {
        return axios({
            method: 'post',
            url: url.delResource,
            data: Qs.stringify({
                ids: ids
            }, {indices: false})
        })
    }

    public static add(data: any): Promise<AxiosResponse> {
        return axios({
            method: 'post',
            url: url.addResource,
            data: Qs.stringify((data))
        })
    }

    public static checkResourceName(resourceName: string, originalValue?: string): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.checkResourceName,
            params: {
                checkValue: resourceName,
                originalValue: originalValue
            }
        })
    }
}
