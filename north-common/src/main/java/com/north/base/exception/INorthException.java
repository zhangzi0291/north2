package com.north.base.exception;

import java.util.Objects;

public interface INorthException {

    /**
     * 异常消息
     *
     * @return 消息
     */
    String getMsg();

    /**
     * 异常码
     *
     * @return 异常码
     */
    int getCode();

    /**
     * 异常码
     *
     * @return 异常码
     */
    int getHttpCode();

    Object getData();

    /**
     * 断言为 true, 就抛出异常
     *
     * @param v1 断言值
     * @throws NorthException e
     */
    default void assertTrueThrows(boolean v1) throws NorthException {
        if (v1) {
            throw new NorthException(this);
        }
    }

    /**
     * 断言必须是 true, 否则抛出异常
     *
     * @param v1 断言值
     * @throws NorthException e
     */
    default void assertTrue(boolean v1) throws NorthException {
        if (v1) {
            return;
        }

        throw new NorthException(this);
    }

    /**
     * 断言必须是 非null, 否则抛出异常
     *
     * @param value 断言值
     * @throws NorthException e
     */
    default void assertNonNull(Object value) throws NorthException {
        assertTrue(Objects.nonNull(value));
    }

    /**
     * 断言必须是 false, 否则抛出异常
     *
     * @param v1 断言值
     * @throws NorthException e
     */
    default void assertFalse(boolean v1) throws NorthException {
        this.assertTrue(!v1);
    }

    /**
     * 断言必须是 false, 否则抛出异常
     *
     * @param v1  断言值
     * @param msg 自定义消息
     * @throws NorthException e
     */
    default void assertFalse(boolean v1, String msg) throws NorthException {
        this.assertTrue(!v1, msg);
    }

    default void assertFalse(boolean v1, String msg, Object data) throws NorthException {
        this.assertTrue(!v1, msg, data);
    }

    /**
     * 断言必须是 true, 否则抛出异常
     *
     * @param v1  断言值
     * @param msg 自定义消息
     * @throws NorthException e
     */
    default void assertTrue(boolean v1, String msg) throws NorthException {
        if (v1) {
            return;
        }
        int code = this.getCode();
        int httpCode = this.getHttpCode();
        throw new NorthException(httpCode, code, msg, null);
    }

    default void assertTrue(boolean v1, String msg, Object data) throws NorthException {
        if (v1) {
            return;
        }
        int code = this.getCode();
        int httpCode = this.getHttpCode();
        throw new NorthException(httpCode, code, msg, data);
    }
}
