package com.zetres.elections.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class GeoPrefectureDTO {

    private Integer pk;

    @NotNull
    @Size(max = 3)
    private String code;

    @NotNull
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String shortName;

    private Integer active;

    @NotNull
    private Integer fkRegion;

    public Integer getPk() {
        return pk;
    }

    public void setPk(final Integer pk) {
        this.pk = pk;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(final String shortName) {
        this.shortName = shortName;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(final Integer active) {
        this.active = active;
    }

    public Integer getFkRegion() {
        return fkRegion;
    }

    public void setFkRegion(final Integer fkRegion) {
        this.fkRegion = fkRegion;
    }

}
