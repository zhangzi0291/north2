package com.north.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.north.sys.entity.SysResource;
import com.north.sys.entity.SysRole;
import com.north.sys.entity.SysUser;

import java.util.List;

/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author NorthZX
 * @since 2020-12-29
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 通过UserId获取对应的角色列表
     *
     * @param userId
     * @return
     */
    List<SysRole> getUserRole(String userId);

    /**
     * 通过UserId获取对应的资源列表
     *
     * @param userId
     * @return
     */
    List<SysResource> getUserResource(String userId);

    /**
     * 检查登陆名和密码是否可以登陆
     *
     * @param usernaem
     * @param password 密码为明文密码md5编码之后的值
     * @return
     */
    SysUser checkUserLogin(String usernaem, String password);

    /**
     * 检查用户的密码是否正确
     *
     * @param userId
     * @param password 密码为明文密码md5编码之后的值
     * @return
     */
    boolean checkPassword(String userId, String password);

    /**
     * 检查登陆名是否重复，不重复返回true，重复返回false
     *
     * @param username
     * @return
     */
    Boolean checkUsername(String username);

    /**
     * 检查用户名是否重复，不重复返回true，重复返回false
     *
     * @param nickname
     * @return
     */
    Boolean checkNickname(String nickname);
}
