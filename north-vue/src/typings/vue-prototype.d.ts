import {AxiosInstance} from 'axios'
import {App} from 'vue'

declare module 'Vue/types/vue' {
    interface Vue {
        $axios: AxiosInstance
    }
}

declare global {
    interface Window {
        vm: App;
        title: string;
        appText: string;
        BASE_URL: string;
        BASE_WS_URL: string;
        websocket: WebSocket;
        isDev: boolean;
    }

    interface Date {
        Format: Function
    }
}
