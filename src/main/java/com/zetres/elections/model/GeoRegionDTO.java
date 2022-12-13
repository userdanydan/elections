package com.zetres.elections.model;

import javax.validation.constraints.Size;


public class GeoRegionDTO {

    private Integer pk;

    @Size(max = 255)
    private String name;

    private Integer active;

    public Integer getPk() {
        return pk;
    }

    public void setPk(final Integer pk) {
        this.pk = pk;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(final Integer active) {
        this.active = active;
    }

}
