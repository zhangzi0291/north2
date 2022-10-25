import {AxiosResponse} from "axios"
import Qs from "qs";
import HttpClient, {RespData} from "@/plugins/axios/HttpClient";
import {BaseCrulApi} from "@/api/BaseApi";

const url = {

    addMate: "/jsonTableMate/addMateList",
    getMate: "/jsonTableMate/getMateList",
    getTableValue: "/jsonTableValue/getTableValue",
    addValue: "/jsonTableValue/add",
    delValue: "/jsonTableValue/del",
    delAllValue: "/jsonTableValue/delAll",

}


export class JsonTableDataAPi extends BaseCrulApi {

    constructor() {
        super({
            list: "/jsonTable/list",
            get: "/jsonTable/get",
            del: "/jsonTable/del",
        });
    }

    public list(tableId: string, data: any, page?: any, sort?: any): Promise<AxiosResponse<RespData>> {
        data.tableId = tableId
        return HttpClient.request({
            method: 'get',
            url: url.getTableValue,
            params: Object.assign(data, page, sort)
        })
    }
}

export class JsonTableAPi extends BaseCrulApi {

    constructor() {
        super({
            list: "/jsonTable/list",
            get: "/jsonTable/get",
            del: "/jsonTable/del",
        });
    }

    public addMate(tableId: any, data: any): Promise<AxiosResponse<RespData>> {
        const mateList: any = []
        data.forEach((d: any) => {
            mateList.push(JSON.stringify(d))
        })
        return HttpClient.request({
            method: 'post',
            url: url.addMate,
            data: {
                tableId: tableId,
                mateList: JSON.stringify(data)
            }
        })
    }

    public getMate(tableId: any): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'post',
            url: url.getMate,
            data: Qs.stringify({
                tableId: tableId
            })
        })
    }

    public valueList(tableId: string, data: any, page?: any, sort?: any): Promise<AxiosResponse<RespData>> {
        data.tableId = tableId
        return HttpClient.request({
            method: 'get',
            url: url.getTableValue,
            params: Object.assign(data, page, sort)
        })
    }

    public addValue(tableId: string, data: any): Promise<AxiosResponse<RespData>> {
        data.tableId = tableId
        return HttpClient.request({
            method: 'get',
            url: url.addValue,
            params: Qs.stringify({
                tableId: tableId,
                jsonValue: JSON.stringify(data)
            })
        })
    }

    public delValue(ids: Array<string>): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'post',
            url: url.delValue,
            data: Qs.stringify({
                ids: ids
            }, {indices: false})
        })
    }

    public delAllValue(tableId: string): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'post',
            url: url.delAllValue,
            data: Qs.stringify({
                tableId: tableId
            })
        })
    }
}

const api = {
    JsonTableDataAPi: new JsonTableDataAPi(),
    JsonTableAPi: new JsonTableAPi(),
}

export default api