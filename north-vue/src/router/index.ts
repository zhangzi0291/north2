import {createRouter, createWebHashHistory} from 'vue-router'

var About = () => import(/* webpackChunkName: "About" */ '@/views/About.vue')
var Index = () => import(/* webpackChunkName: "Index" */ '@/views/Index.vue')
var Home = () => import(/* webpackChunkName: "Home" */ '@/views/home/HomePage.vue')

var Login = () => import(/* webpackChunkName: "Login" */ '@/views/login/LoginPage.vue')

var SysResource = () => import(/* webpackChunkName: "SysResource" */ '@/views/sys/sysresource/SysResourcePage.vue')
var SysUser = () => import(/* webpackChunkName: "SysUser" */ '@/views/sys/sysuser/SysUserPage.vue')
var SysOnlineUser = () => import(/* webpackChunkName: "SysOnlineUser" */ '@/views/sys/onlineuser/SysOnlineUserPage.vue')
var SysRole = () => import(/* webpackChunkName: "SysRole" */ '@/views/sys/sysrole/SysRolePage.vue')
var SysDict = () => import(/* webpackChunkName: "SysDict" */ '@/views/sys/sysdict/SysDictPage.vue')
var SysArea = () => import(/* webpackChunkName: "SysArea" */ '@/views/sys/sysarea/SysAreaPage.vue')
var SysLog = () => import(/* webpackChunkName: "SysLog" */ '@/views/sys/syslog/SysLogPage.vue')

var GenealogyTree = () => import(/* webpackChunkName: "GenealogyTree" */ '@/views/genealogy/genealogyTree/GenealogyTreePage.vue')
var GenealogyTreeGraph = () => import(/* webpackChunkName: "GenealogyTreeGraph" */ '@/views/genealogy/genealogyTree/GenealogyTree.vue')


var IframePanel = () => import(/* webpackChunkName: "test" */ '@/views/IframePanel.vue')

var test = () => import(/* webpackChunkName: "IframePanel" */ '@/views/test.vue')


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
            {
                path: 'genealogy/tree',
                name: '族谱',
                component: GenealogyTree,
            },

        ]
    },
    {
        path: '/login',
        name: 'login',
        component: Login,
    },
    {
        path: '/genealogyTreeGraph',
        name: 'GenealogyTreeGraph',
        component: GenealogyTreeGraph,
    },
]

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

export default router
