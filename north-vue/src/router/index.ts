import {createRouter, createWebHashHistory} from 'vue-router'

var About = () => import(/* webpackChunkName: "About" */ '@/views/About.vue')
var Index = () => import(/* webpackChunkName: "Index" */ '@/views/Index.vue')
var Home = () => import(/* webpackChunkName: "Home" */ '@/views/home/Home.vue')
var Login = () => import(/* webpackChunkName: "Login" */ '@/views/login/Login.vue')
var SysResource = () => import(/* webpackChunkName: "SysResource" */ '@/views/sys/sysresource/SysResource.vue')
var SysUser = () => import(/* webpackChunkName: "SysUser" */ '@/views/sys/sysuser/SysUser.vue')
var SysOnlineUser = () => import(/* webpackChunkName: "SysOnlineUser" */ '@/views/sys/onlineuser/SysOnlineUser.vue')
var SysRole = () => import(/* webpackChunkName: "SysRole" */ '@/views/sys/sysrole/SysRole.vue')
var SysDict = () => import(/* webpackChunkName: "SysDict" */ '@/views/sys/sysdict/SysDict.vue')
var SysArea = () => import(/* webpackChunkName: "SysArea" */ '@/views/sys/sysarea/SysArea.vue')
var SysLog = () => import(/* webpackChunkName: "SysLog" */ '@/views/sys/syslog/SysLog.vue')
var IframePanel = () => import(/* webpackChunkName: "IframePanel" */ '@/views/IframePanel.vue')

const routes = [
    {
        path: '/',
        name: '/',
        redirect: '/home',
        component: Index,
        children: [
            {
                path: 'home',
                name: '首页',
                component: Home,
            },
            {
                path: 'iframePanel',
                name: '内嵌页面',
                component: IframePanel,
            },
            {
                path: 'sysresource/list',
                name: '资源管理',
                component: SysResource,
            },
            {
                path: 'sysuser/list',
                name: '用户管理',
                component: SysUser,
            },
            {
                path: 'sysonlineuser/list',
                name: '在线用户',
                component: SysOnlineUser,
            },
            {
                path: 'sysrole/list',
                name: '角色管理',
                component: SysRole,
            },
            {
                path: 'sysdict/list',
                name: '字典管理',
                component: SysDict,
            },
            {
                path: 'sysonlineuser/list',
                name: '区域管理',
                component: SysOnlineUser,
            },
            {
                path: 'sysarea/list',
                name: '区域管理',
                component: SysArea,
            },
            {
                path: 'syslog/list',
                name: '日志管理',
                component: SysLog,
            },

        ]
    },
    {
        path: '/login',
        name: 'login',
        component: Login,
    },
]

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

export default router
