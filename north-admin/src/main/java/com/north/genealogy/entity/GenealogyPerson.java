package com.north.genealogy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.north.base.DeleteLogicBaseModel;

import java.time.LocalDateTime;

/**
 * <p>
 * 族谱中的人信息
 * </p>
 *
 * @author NorthZX
 * @since 2021-04-14
 */
@TableName("genealogy_person")
public class GenealogyPerson extends DeleteLogicBaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 姓名
     */
    private String personName;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 出生日期
     */
    private LocalDateTime birthday;

    /**
     * 逝世日期
     */
    private LocalDateTime deathday;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
    }

    public LocalDateTime getDeathday() {
        return deathday;
    }

    public void setDeathday(LocalDateTime deathday) {
        this.deathday = deathday;
    }

    @Override
    public String toString() {
        return "GenealogyPerson{" +
                "id=" + id +
                ", personName=" + personName +
                ", sex=" + sex +
                ", birthday=" + birthday +
                ", deathday=" + deathday +
                "}";
    }
}
