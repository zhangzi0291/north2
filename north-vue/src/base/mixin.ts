const computed = {
    user() {
        let user = <any>window.localStorage.getItem("user")
        if (user != null) {
            return JSON.parse(user)
        }
        return {};
    },
    isAdmin() {
        let user = <any>window.localStorage.getItem("user")
        return user.username == "admin"
    },
}
const methods = {
    //是否含有指定权限 [指定多个，只要其一验证通过即可]，admin用户全部通过
    hasPermissionOr(...permission: string[]) {
        // @ts-ignore
        if (this.isAdmin) {
            return true
        }
        let permissions = window.localStorage.getItem("permissions")
        if (permissions != null) {
            let ps = JSON.parse(permissions)
            for (let p of permission) {
                if (ps.includes(p)) {
                    return true
                }
            }
        }
        return false;
    },
    //是否含有指定权限 [指定多个，必须全部验证通过]，admin用户全部通过
    hasPermissionAnd(...permission: string[]) {
        // @ts-ignore
        if (this.isAdmin) {
            return true
        }
        let length = permission.length;
        let pLength = 0;
        let permissions = window.localStorage.getItem("permissions")
        if (permissions != null) {
            let ps = JSON.parse(permissions)
            for (let p of permission) {
                if (ps.includes(p)) {
                    pLength++
                }
            }
        }
        return length == pLength;
    },
    //是否含有指定角色 [指定多个，只要其一验证通过即可]，admin用户全部通过
    hasRoleOr(...role: string[]) {
        // @ts-ignore
        if (this.isAdmin) {
            return true
        }
        let roles = window.localStorage.getItem("roles")
        if (roles != null) {
            let rs = JSON.parse(roles)
            for (let r of role) {
                if (rs.includes(r)) {
                    return true
                }
            }
        }
        return false;
    },
    //是否含有指定角色 [指定多个，必须全部验证通过]，admin用户全部通过
    hasRoleAnd(...role: string[]) {
        // @ts-ignore
        if (this.isAdmin) {
            return true
        }
        let length = role.length;
        let pLength = 0;
        let roles = window.localStorage.getItem("roles")
        if (roles != null) {
            let rs = JSON.parse(roles)
            for (let r of role) {
                if (rs.includes(r)) {
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

export default {computed,methods}