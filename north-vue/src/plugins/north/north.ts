import {App} from 'vue';
import BasePage from '@/components/base/page/BasePage.vue'
import HtmlPanel from '@/components/HtmlPanel.vue'
import {AxiosResponse} from "axios";

import MdEditor from 'md-editor-v3';
import 'md-editor-v3/lib/style.css';
import UploadImage from "@/components/UploadImage.vue";

var install = function install(app: App) {

    app.component("md-editor", MdEditor)
    app.component("base-page", BasePage)
    app.component("html-panel", HtmlPanel)
    app.component("upload-image", UploadImage)

    app.config.globalProperties.$download = function (url: string, method: string, fileName: string, data: any) {
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

    Date.prototype.Format = function (fmt: string) {
        var o = {
            "M+": this.getMonth() + 1,                 //月份
            "d+": this.getDate(),                    //日
            "h+": this.getHours(),                   //小时
            "m+": this.getMinutes(),                 //分
            "s+": this.getSeconds(),                 //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds()             //毫秒
        };
        if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }
}


var _default = {
    version: 1,
    install: install
};

export default _default;