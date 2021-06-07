export default class BaseApi {

    public static setUserByStorage(user: any) {
        window.localStorage.setItem("user", JSON.stringify(user))
    }

    public static setTokenByStorage(token: string) {
        window.localStorage.setItem("token", token)
    }

    public static setPermissionsByStorage(permissions: any) {
        window.localStorage.setItem("permissions", JSON.stringify(permissions))

    }

    public static setRolesByStorage(roles: any) {
        window.localStorage.setItem("roles", JSON.stringify(roles))
    }

    public static getUserByStorage() {
        let user = window.localStorage.getItem("user")
        if (user != null) {
            return JSON.parse(user)
        }
        return {};
    }

    public static getTokenByStorage() {
        return window.localStorage.getItem("token")
    }

    public static getPermissionsByStorage() {
        let permissions = window.localStorage.getItem("permissions")
        if (permissions != null) {
            return JSON.parse(permissions)
        }
        return [];
    }

    public static getRolesByStorage() {
        let roles = window.localStorage.getItem("roles")
        if (roles != null) {
            return JSON.parse(roles)
        }
        return []
    }

    public static setWsIsClose(isClose:boolean) {
        window.localStorage.setItem("wsIsClose", JSON.stringify(isClose))
    }

    public static getWsIsClose() {
        let isClose = window.localStorage.getItem("wsIsClose")
        if(isClose !=null){
            return !!JSON.parse(isClose)
        }
        return false;
    }




}
