import {createApp} from 'vue'

import App from '@/App.vue'

import router from './router'

import Antd from '@/plugins/antd';
import axios from '@/plugins/axios';
import north from '@/plugins/north/north';

import mixin from '@/base/mixin';

const app = createApp(App)

app.mixin({
    computed: mixin.computed,
    methods: mixin.methods
});

window.vm = app


app.use(router)
    .use(Antd)
    .use(axios)
    .use(north)
    .mount('#app')


