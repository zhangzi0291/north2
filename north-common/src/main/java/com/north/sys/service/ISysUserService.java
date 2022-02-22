package com.north.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.north.sys.entity.SysResource;
import com.north.sys.entity.SysRole;
import com.north.sys.entity.SysUser;

import java.util.List;
import java.util.Map;

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
    SysUser checkCanUserLogin(String usernaem, String password);

    /**
     * 校验验证码是否通过
     * @param genId
     * @return
     */
    Boolean checkGen(String genId);
    /**
     * 登录
     *
     * @param user
     */
    void login(SysUser user, String deviceType);

    /**
     * 登陆，并设置超时时间
     *
     * @param user
     * @param deviceType
     */
    void login(SysUser user, String deviceType, Long timeout);

    /**
     * 注销
     *
     * @param deviceType
     */
    void logout(String deviceType);

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
     * 检查昵称是否重复，不重复返回true，重复返回false
     *
     * @param nickname
     * @return
     */
    Boolean checkNickname(String nickname);

    /**
     * 获取在线用户总数
     *
     * @return
     */
    Long getTotalOnlineNum();

    /**
     * 获取用户权限
     *
     * @return
     */
    List<String> getPermissionLis(String userId);

    /**
     * 获取用户角色
     *
     * @return
     */
    List<String> getRoleList(String userId);

    /**
     * 生成验证码，并保存到redis待验证
     *
     * @param userId      reids保存的key，用户id
     * @param invalidTime
     * @return
     */
    String createVerificationCode(String userId, Integer invalidTime);

    /**
     * 校验验证码
     *
     * @param userId
     * @param verificationCode
     * @return
     */
    Boolean checkVerificationCode(String userId, String verificationCode);

    /**
     * 导入用户和相应的权限
     * map的key user:SysUser 用户,roleList:List<SysRole> 角色列表
     *
     * @param list
     */
    void importSysUserList(List<Map<String, Object>> list);
}
