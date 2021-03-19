import {AxiosResponse} from "axios"
import Qs from "qs";

const app = window.vm

const axios = app.config.globalProperties.$axios

const url = {
    getMenu: "/sysResource/getMenuTree?pid=-1",
    getAllResource: "/sysResource/getResourceTree?pid=-1",
    addResource: "/sysResource/add",
    delResource: "/sysResource/del",
    checkResourceName: "/sysResource/checkResourceName",

}


export default class SysResourceApi {

    public getMenu(): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.getMenu,
        })
    }

    public getAllResource(): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.getAllResource,
        })
    }

    public del(ids: Array<string>): Promise<AxiosResponse> {
        return axios({
            method: 'post',
            url: url.delResource,
            data: Qs.stringify({
                ids: ids
            }, {indices: false})
        })
    }

    public add(data: any): Promise<AxiosResponse> {
        return axios({
            method: 'post',
            url: url.addResource,
            data: Qs.stringify((data))
        })
    }

    public checkResourceName(resourceName: string, originalValue?: string): Promise<AxiosResponse> {
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
