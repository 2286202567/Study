package com.lz.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public class Permission {
    private Integer  id;
    private String name;
    private String resource;
    private Integer state;
    private Integer  menuId;
    private String expression;

    public Permission(Integer id, String name, String resource, Integer state, Integer menuId, String expression) {
        this.id = id;
        this.name = name;
        this.resource = resource;
        this.state = state;
        this.menuId = menuId;
        this.expression = expression;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", resource='" + resource + '\'' +
                ", state=" + state +
                ", menuId=" + menuId +
                ", expression='" + expression + '\'' +
                '}';
    }
}
