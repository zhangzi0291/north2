import {App} from 'vue';
import axios from "axios";

declare let BASE_URL: any;

var install = function install(app: App) {

    var baseUrl = BASE_URL
    // 获取路由器实例
    const router = app.config.globalProperties.$router
    // route是响应式对象,可监控器变化
    // const route = useRoute()

    axios.defaults.baseURL = baseUrl;
    app.config.globalProperties.BASE_URL = baseUrl;

    //使用cookie 需要注意set-cookie中是否有 HttpOnly; SameSite=none
    axios.defaults.withCredentials = true;

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
    axios.interceptors.response.use(response => {
        if (response.data.code == 500 || response.data.code == 403) {
            app.config.globalProperties.$message.error(response.config.url + " " + response.data.msg)
            throw new Error(response.config.url + " " + response.data.msg)
        }
        return response;
    }, error => {
        if (error.response == undefined) {
            app.config.globalProperties.$message.error("服务器无响应")
        }
        if(!error.response){
            return Promise.reject(error);
        }
        if (error.response.status === 401) {
            return router.push({path: '/login',})
        } else if (error.response.status === 403) {
            app.config.globalProperties.$message.error(error.response.config.url + " 权限不足")
            return Promise.reject(error);
        }

        if (error.response.status == 500) {
            app.config.globalProperties.$message.error(
                error.response.config.url + " " + error.response.data.msg
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

    app.config.globalProperties.$axios = axios;


    app.provide('$axios', axios)
}


var _default = {
    version: 1,
    install: install
};

export default _default;