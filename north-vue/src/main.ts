import {createApp} from 'vue'
import App from '@/App.vue'
import router from './router'

import Antd from '@/plugins/antd';
import axios from '@/plugins/axios';

import north from '@/plugins/north/north';


const app = createApp(App)

app.mixin({
    methods: {
        getCookie(name:string) {
            let allCookies = document.cookie
            console.log(allCookies)
            let arrcookie = allCookies.split(";");
            for (let cookie of arrcookie) {
                let arr = cookie.split("=");
                if(arr[0]==name){
                    return arr[1]
                }
            }
        }
    }
});

window.vm = app


app.use(router)
    .use(Antd)
    .use(axios)
    .use(north)
    .mount('#app')


