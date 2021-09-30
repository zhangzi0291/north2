import {createRouter, createWebHashHistory, createWebHistory} from 'vue-router'

// const About = () => import(/* webpackChunkName: "About" */ '@/views/About.vue')
const Index = () => import(/* webpackChunkName: "Index" */ '@/views/Index.vue')
const Home = () => import(/* webpackChunkName: "Home" */ '@/views/home/HomePage.vue')

const Login = () => import(/* webpackChunkName: "Login" */ '@/views/login/LoginPage.vue')
const SsoLogin = () => import(/* webpackChunkName: "SsoLogin" */ '@/views/login/SsoLoginPage.vue')
const IframePanel = () => import(/* webpackChunkName: "IframePanel" */ '@/views/IframePanel.vue')
/**
 * 后台管理
 */
const SysResource = () => import(/* webpackChunkName: "SysResource" */ '@/views/sys/sysresource/SysResourcePage.vue')
const SysUser = () => import(/* webpackChunkName: "SysUser" */ '@/views/sys/sysuser/SysUserPage.vue')
const SysOnlineUser = () => import(/* webpackChunkName: "SysOnlineUser" */ '@/views/sys/onlineuser/SysOnlineUserPage.vue')
const SysRole = () => import(/* webpackChunkName: "SysRole" */ '@/views/sys/sysrole/SysRolePage.vue')
const SysDict = () => import(/* webpackChunkName: "SysDict" */ '@/views/sys/sysdict/SysDictPage.vue')
const SysArea = () => import(/* webpackChunkName: "SysArea" */ '@/views/sys/sysarea/SysAreaPage.vue')
const SysLog = () => import(/* webpackChunkName: "SysLog" */ '@/views/sys/syslog/SysLogPage.vue')

/**
 * 家族
 */
const GenealogyTree = () => import(/* webpackChunkName: "GenealogyTree" */ '@/views/genealogy/genealogyTree/GenealogyTreePage.vue')
const GenealogyTreeGraph = () => import(/* webpackChunkName: "GenealogyTreeGraph" */ '@/views/genealogy/genealogyTree/GenealogyTree.vue')

/**
 * py小工具
 */
const pyRegister = () => import(/* webpackChunkName: "pyRegister" */ '@/views/pytool/RegisterPage.vue')
const pyChangePwd = () => import(/* webpackChunkName: "pyChangePwd" */ '@/views/pytool/ChangePwdPage.vue')
const pyApp = () => import(/* webpackChunkName: "pyApp" */ '@/views/pytool/app/AppPage.vue')


const test = () => import(/* webpackChunkName: "IframePanel" */ '@/views/test.vue')


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
            // {
            //     path: 'genealogy/tree',
            //     name: '族谱',
            //     component: GenealogyTree,
            // },
            {
                path: "/pytool/app",
                name: "py工具应用列表",
                component: pyApp
            }
        ]
    },
    {
        path: '/login',
        name: 'login',
        component: Login,
    },
    {
        path: '/ssoLogin',
        name: 'ssoLogin',
        component: SsoLogin,
    },
    // {
    //     path: '/genealogyTreeGraph',
    //     name: 'GenealogyTreeGraph',
    //     component: GenealogyTreeGraph,
    // },
    {
        path: '/pytool/register',
        name: 'pytool-register',
        component: pyRegister,
    },
    {
        path: '/pytool/changepwd',
        name: 'pytool-changepwd',
        component: pyChangePwd,
    }
]

const router = createRouter({
    history: createWebHashHistory(),
    routes: routes
})

export default router
