import {createApp} from 'vue'
import App from '@/App.vue'
import router from './router'

import Antd from '@/plugins/antd';
import axios from '@/plugins/axios';

import north from '@/plugins/north/north';


const app = createApp(App)

app.mixin({
    computed:{
        user() {
            let user = window.localStorage.getItem("user")
            if (user != null) {
                return JSON.parse(user)
            }
            return {};
        },
        isAdmin(){
            return this.user.username == "admin"
        },
    },
    methods: {
        //是否含有指定权限 [指定多个，只要其一验证通过即可]，admin用户全部通过
        hasPermissionOr(...permission: string[]){
            if(this.isAdmin){
                return true
            }
            let permissions = window.localStorage.getItem("permissions")
            if (permissions != null) {
                let ps = JSON.parse(permissions)
                console.log(ps)
                for (let p of permission) {
                    if(ps.includes(p)){
                        return true
                    }
                }
            }
            return false;
        },
        //是否含有指定权限 [指定多个，必须全部验证通过]，admin用户全部通过
        hasPermissionAnd(...permission: string[]){
            if(this.isAdmin){
                return true
            }
            let length = permission.length;
            let pLength = 0;
            let permissions = window.localStorage.getItem("permissions")
            if (permissions != null) {
                let ps = JSON.parse(permissions)
                for (let p of permission) {
                    if(ps.includes(p)){
                        pLength++
                    }
                }
            }
            return length == pLength;
        },
        //是否含有指定角色 [指定多个，只要其一验证通过即可]，admin用户全部通过
        hasRoleOr(...role: string[]){
            if(this.isAdmin){
                return true
            }
            let roles = window.localStorage.getItem("roles")
            if (roles != null) {
                let rs = JSON.parse(roles)
                for (let r of role) {
                    if(rs.includes(r)){
                        return true
                    }
                }
            }
            return false;
        },
        //是否含有指定角色 [指定多个，必须全部验证通过]，admin用户全部通过
        hasRoleAnd(...role: string[]){
            if(this.isAdmin){
                return true
            }
            let length = role.length;
            let pLength = 0;
            let roles = window.localStorage.getItem("roles")
            if (roles != null) {
                let rs = JSON.parse(roles)
                for (let r of role) {
                    if(rs.includes(r)){
                        pLength++
                    }
                }
            }
            return length == pLength;
        },
        getCookie(name: string) {
            let allCookies = document.cookie
            console.log(allCookies)
            let arrcookie = allCookies.split(";");
            for (let cookie of arrcookie) {
                let arr = cookie.split("=");
                if (arr[0] == name) {
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


