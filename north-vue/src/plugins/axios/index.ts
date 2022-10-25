import {App} from "vue";
import HttpClient from "@/plugins/axios/HttpClient";


const install = function install(app: App) {
    app.config.globalProperties.$http = HttpClient;
    app.provide('$http', HttpClient)
}


const _default = {
    version: 1,
    install: install
};

export default _default;