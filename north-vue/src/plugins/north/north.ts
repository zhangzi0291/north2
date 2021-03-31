import {App} from 'vue';
import BasePage from '@/components/base/page/BasePage.vue'
import HtmlPanel from '@/components/HtmlPanel.vue'
import * as icons from "@ant-design/icons-vue";
import {AxiosResponse} from "axios";


var install = function install(app: App) {

    app.component("base-page", BasePage)
    app.component("html-panel", HtmlPanel)

    app.config.globalProperties.$download = function(url:string,method:string,fileName:string,data: any){
        app.config.globalProperties.$axios({
            method: method,
            url: url,
            data: Object.assign(data),
            responseType: 'blob',
        }).then((res: AxiosResponse) => {
            const blob = new Blob([res.data])
            var objectUrl = window.URL.createObjectURL(blob)//创建新的URL表示指定Blob对象
            var a = document.createElement('a')//创建a标签
            a.href = objectUrl//指定下载链接
            a.download = fileName//指定下载文件名
            a.click()//触发下载
            a.remove()//除a标签
            window.URL.revokeObjectURL(objectUrl)//释放
        });

    }

}


var _default = {
    version: 1,
    install: install
};

export default _default;