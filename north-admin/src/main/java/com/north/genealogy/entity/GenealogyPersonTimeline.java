package com.north.genealogy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.north.base.BaseModel;

import java.time.LocalDateTime;

/**
 * <p>
 * 族谱中的人时间轴
 * </p>
 *
 * @author NorthZX
 * @since 2021-04-20
 */
@TableName("genealogy_person_timeline")
public class GenealogyPersonTimeline extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 人ID
     */
    private String personId;

    /**
     * 发生时间
     */
    private LocalDateTime eventTime;

    /**
     * 事件描述
     */
    private String story;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    @Override
    public String toString() {
        return "GenealogPersonTimeline{" +
                "id=" + id +
                ", personId=" + personId +
                ", eventTime=" + eventTime +
                ", story=" + story +
                "}";
    }
}
