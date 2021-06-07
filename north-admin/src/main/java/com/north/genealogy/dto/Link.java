package com.north.genealogy.dto;

import java.util.StringJoiner;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-04-14
 */
public class Link {

    private String source;
    private String target;
    private String relationship;

    public Link() {
    }

    public Link(String source, String target, String relationship) {
        this.source = source;
        this.target = target;
        this.relationship = relationship;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Link.class.getSimpleName() + "[", "]")
                .add("source='" + source + "'")
                .add("target='" + target + "'")
                .add("relationship='" + relationship + "'")
                .toString();
    }
}
