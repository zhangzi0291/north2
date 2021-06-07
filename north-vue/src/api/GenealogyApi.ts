import {AxiosResponse} from "axios"
import Qs from "qs";

const app = window.vm

const axios = app.config.globalProperties.$axios

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


export default class GenealogyApi {

    public static getGenealogyTree(genealogyId:string): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.getGenealogyTree,
            params:{
                genealogyId: genealogyId
            }
        })
    }

    public static getGenealogyTreePersonSearch(genealogyId:string,value:string): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.getGenealogyTreePersonSearch,
            params:{
                genealogyId: genealogyId,
                personName:value
            }
        })
    }

    public static getGenealogyPerson(id:string): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.getGenealogyPerson,
            params:{
                id: id
            }
        })
    }

    public static addGenealogyPerson(data:FormData): Promise<AxiosResponse> {
        return axios({
            method: 'post',
            url: url.addGenealogyPerson,
            data: data
        })
    }

    public static editGenealogyPerson(data:FormData): Promise<AxiosResponse> {
        return axios({
            method: 'post',
            url: url.editGenealogyPerson,
            data: data
        })
    }

    public static removeGenealogyPerson(id:string): Promise<AxiosResponse> {
        return axios({
            method: 'get',
            url: url.removeGenealogyPerson,
            params:{
                id: id
            }
        })
    }

    public static getGenealogyPersonImages(id:string): Promise<AxiosResponse> {
        return axios({
            method: 'post',
            url: url.getGenealogyPersonImages,
            data: Qs.stringify({
                id: id
            })
        })
    }

    public static getGenealogyPersonTimeLines(id:string): Promise<AxiosResponse> {
        return axios({
            method: 'post',
            url: url.getGenealogyPersonTimeLines,
            data: Qs.stringify({
                id: id
            })
        })
    }
}
