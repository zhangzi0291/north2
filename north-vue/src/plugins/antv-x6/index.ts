import {App} from 'vue';
import {Graph} from "@antv/x6";
import NodeCell from "@/components/genealogy/nodeTheme/NodeCell.vue";
import "@antv/x6-vue-shape"


var install = function install(app: App) {
    Graph.registerNode("PersonCell", {
        inherit: "vue-shape",
        width: 100,
        height: 100,
        component: {
            template: `
            <node-cell/>`,
            components: {
                NodeCell
            }
        }
    })
}


var _default = {
    version: 1,
    install: install
};

export default _default;