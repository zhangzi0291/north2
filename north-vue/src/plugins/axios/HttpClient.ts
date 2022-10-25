import axios, {AxiosInstance, AxiosRequestConfig, AxiosResponse} from 'axios'
import router from '@/router'

const app = window.vm

export type RespData<T = any> = {
    code: 0 | number // 0 => ok
    msg: string
    data: T
}

class Api {

    config: AxiosRequestConfig
    instance: AxiosInstance

    constructor(option: AxiosRequestConfig) {
        this.config = option
        // 配置全局参数
        this.instance = axios.create(this.config)

        // const BASE_URL = window.BASE_URL
        // this.instance.defaults.baseURL = BASE_URL;
        //使用cookie 需要注意set-cookie中是否有 HttpOnly; SameSite=none
        // this.instance.defaults.withCredentials = true;

        this.interceptors(this.instance)
    }

    interceptors(axios: AxiosInstance) {
        const SSO_URL = window.SSO_URL
        //请求前置
        axios.interceptors.request.use(
            config => {
                // let param = new URLSearchParams();
                // if(config.data instanceof FormData){
                //   config.headers["content-type"] = "multipart/form-data";
                // } else if (!!config.data&(!config.headers.isjson)) {
                //   config.data = getParams(config.data);
                // }
                // config.headers["X-Requested-With"] = "XMLHttpRequest";
                return config;
            },
            error => {
                // 对请求错误做些什么
                console.log(error);
                return Promise.reject(error);
            }
        );

        //响应前置
        //错误统一处理
        axios.interceptors.response.use(response => {
            if (response.data.code == 500 || response.data.code == 403) {
                if (window.isDev) {
                    app.config.globalProperties.$message.error(response.config.url + " " + response.data.msg)
                } else {
                    app.config.globalProperties.$message.error(response.data.msg)
                }
                throw new Error(response.config.url + " " + response.data.msg)
            }
            return response;
        }, error => {
            console.log(error)
            if (error.response == undefined) {
                app.config.globalProperties.$message.error("服务器无响应")
            }
            if (!error.response) {
                return Promise.reject(error);
            }

            if (error.response.status === 401) {
                const href = window.location.href.substring(0, window.location.href.indexOf("#") == -1 ? window.location.href.length : window.location.href.indexOf("#"))
                const hash = window.location.hash.substring(1, window.location.hash.length)
                console.log(href, hash)
                if (SSO_URL) {
                    axios.get(SSO_URL + "sso-server/getSsoAuthUrl?redirect=" + href + "&hash=" + hash).then(res => {
                        window.location.href = res.data.data
                    })
                } else {
                    return router.push({path: '/login',})
                }
            } else if (error.response.status === 403) {
                app.config.globalProperties.$message.error(error.response.config.url + " 权限不足")
                return Promise.reject(error);
            }

            if (error.response.status == 500) {
                app.config.globalProperties.$message.error(
                    error.response.config.url + " " + error.response.data.msg
                )
            }

            if (error.response.status == 550) {
                const blob = new Blob([error.response.data])
                console.log(window.URL)
                const objectUrl = window.URL.createObjectURL(blob)//创建新的URL表示指定Blob对象
                const a = document.createElement('a')//创建a标签
                a.href = objectUrl//指定下载链接
                a.download = "错误报告.xlsx"//指定下载文件名
                a.click()//触发下载
                a.remove()//除a标签
                window.URL.revokeObjectURL(objectUrl)//释放
                app.config.globalProperties.$message.error(
                    error.response.config.url + " 导入失败，下载错误报告"
                )
            }
            // if(error.response.status != 200 || error.response.status != 302) {
            //   if(error.response.data.status==500){
            //     // app.config.globalProperties.$message.error(
            //     //   error.response.data.error
            //     //   +"<br>"+
            //     //   error.response.data.message
            //     // )
            //   }
            //   if(error.response.data.msg){
            //     // app.config.globalProperties.$message.error(error.response.data.msg)
            //   }
            // }
            return Promise.reject(error);
        });

    }

    public async request<T = RespData>(config: AxiosRequestConfig): Promise<AxiosResponse<T>> {
        return this.instance.request<T, AxiosResponse<T>>(config)
    }

}


const api = new Api({
    baseURL: window.BASE_URL,
    withCredentials: true,
    // timeout: 10 * 1000,
})

export default api