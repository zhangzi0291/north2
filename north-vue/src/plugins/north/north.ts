import {App} from 'vue';
import BasePage from '@/components/base/page/BasePage.vue'
import HtmlPanel from '@/components/HtmlPanel.vue'


var install = function install(app: App) {

    app.component("base-page", BasePage)
    app.component("html-panel", HtmlPanel)

}


var _default = {
    version: 1,
    install: install
};

export default _default;