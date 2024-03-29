import {App} from 'vue';
import *  as icons from '@ant-design/icons-vue';
import Antd, {message, Modal, notification} from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
import {Component} from "@vue/runtime-core";


const install = function install(app: App) {
    let iconName: string[] = []
    Object.keys(icons).forEach((key) => {
        app.component(key, <Component>icons[key])
    })

    app.config.globalProperties.$icons = icons
    app.config.globalProperties.$modal = Modal
    app.config.globalProperties.$message = message
    app.config.globalProperties.$notification = notification

    app.use(Antd)
}


const _default = {
    version: 1,
    install: install
};

export default _default;