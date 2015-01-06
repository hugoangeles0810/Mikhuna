package com.jasoftsolutions.mikhuna.domain;

import java.io.Serializable;

/**
* Created by pc07 on 30/04/2014.
*/
public class SelectOption implements Serializable {
    private Long id;
    private String name;

    public SelectOption() {
    }

    public SelectOption(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
