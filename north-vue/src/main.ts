import {createApp} from 'vue'

import App from '@/App.vue'

import router from './router'

import store from '@/store';
import Antd from '@/plugins/antd';
import axios from '@/plugins/axios';
import north from '@/plugins/north/north';
import x6 from '@/plugins/antv-x6';

import mixin from '@/base/mixin';

const app = createApp(App)

app.mixin({
    computed: mixin.computed,
    methods: mixin.methods
});

window.vm = app


app.use(router)
    .use(store)
    .use(Antd)
    .use(x6)
    .use(axios)
    .use(north)
    .mount('#app')


