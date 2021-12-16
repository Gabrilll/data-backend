package com.judicature.databackend.po;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.List;

@Node
@AllArgsConstructor
@NoArgsConstructor
public class Relation {
    @Id
    private Long identity;
    private Long start;
    private Long end;
    private String type;
    private List<Property> properties;

    public Long getIdentity() {
        return identity;
    }

    public void setIdentity(Long identity) {
        this.identity = identity;
    }

    public Long getEnd() {
        return end;
    }

    public Long getStart() {
        return start;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
}
