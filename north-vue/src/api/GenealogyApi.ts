import {AxiosResponse} from "axios"
import Qs from "qs";
import HttpClient, {RespData} from "@/plugins/axios/HttpClient";


const url = {
    getGenealogyTree: "/genealogy/getGenealogyTree",
    getGenealogyTreePersonSearch: "/genealogy/getGenealogyTreePersonSearch",
    getGenealogyPerson: "/genealogyPerson/get",
    addGenealogyPerson: "/genealogyPerson/addGenealogyPerson",
    editGenealogyPerson: "/genealogyPerson/editGenealogyPerson",
    removeGenealogyPerson: "/genealogyPerson/removeGenealogyPerson",
    getGenealogyPersonImages: "/genealogyPerson/getGenealogyPersonImages",
    getGenealogyPersonTimeLines: "/genealogyPerson/getGenealogyPersonTimeLines",
}


class GenealogyApi {

    public getGenealogyTree(genealogyId: string): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'get',
            url: url.getGenealogyTree,
            params: {
                genealogyId: genealogyId
            }
        })
    }

    public getGenealogyTreePersonSearch(genealogyId: string, value: string): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'get',
            url: url.getGenealogyTreePersonSearch,
            params: {
                genealogyId: genealogyId,
                personName: value
            }
        })
    }

    public getGenealogyPerson(id: string): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'get',
            url: url.getGenealogyPerson,
            params: {
                id: id
            }
        })
    }

    public addGenealogyPerson(data: FormData): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'post',
            url: url.addGenealogyPerson,
            data: data
        })
    }

    public editGenealogyPerson(data: FormData): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'post',
            url: url.editGenealogyPerson,
            data: data
        })
    }

    public removeGenealogyPerson(id: string): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'get',
            url: url.removeGenealogyPerson,
            params: {
                id: id
            }
        })
    }

    public getGenealogyPersonImages(id: string): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'post',
            url: url.getGenealogyPersonImages,
            data: Qs.stringify({
                id: id
            })
        })
    }

    public getGenealogyPersonTimeLines(id: string): Promise<AxiosResponse<RespData>> {
        return HttpClient.request({
            method: 'post',
            url: url.getGenealogyPersonTimeLines,
            data: Qs.stringify({
                id: id
            })
        })
    }
}

const api = new GenealogyApi()
export default api