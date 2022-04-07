package com.north.base.exception;

/**
 * @author Northzx
 * @version 1.0
 * @since 2020-12-30
 */
public class LoginFailedException extends NorthBaseException {

    public LoginFailedException(LoginFailedEnum type) {
        super(type.getMessage());
        this.type = type;
    }

    /**
     * 异常类型
     */
    private final LoginFailedEnum type;

    /**
     * 获取异常类型
     *
     * @return
     */
    public LoginFailedEnum getType() {
        return type;
    }

    public static LoginFailedException newInstance(LoginFailedEnum type) {
        return new LoginFailedException(type);
    }


    public enum LoginFailedEnum {

        UNKNOWN_ERROR("-1", "登陆错误"),
        USER_PWD_ERROR("1", "用户名或密码错误"),
        EXPIRED_ERROR("2", "用户过期"),
        LOCKING_ERROR("3", "用户锁定"),
        GEN_ERROR("4", "验证码校验失败");
        private final String type;
        private final String message;

        LoginFailedEnum(String type, String message) {
            this.type = type;
            this.message = message;
        }

        public String getType() {
            return type;
        }

        public String getMessage() {
            return message;
        }
    }
}
