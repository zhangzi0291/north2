import {App} from 'vue'
import {AxiosStatic} from "axios";
import {Store} from "vuex";

declare module '*.vue' {
    import Vue from 'vue'
    export default Vue
}

declare module "@vue/runtime-core" {
    interface ComponentCustomProperties {
        $store: Store,
        $message: any,
        $icons: any,
        $modal: any,
        $notification: any,
        $http: AxiosStatic,
        $download: any,
        $store:Store,
    }
}

declare global {
    interface Window {
        vm: App;
        title: string;
        appText: string;
        BASE_URL: string;
        SSO_URL: string;
        BASE_WS_URL: string;
        websocket: WebSocket;
        isDev: boolean;

    }

    interface Date {
        Format: Function<string>
    }
}


