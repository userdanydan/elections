package com.zetres.elections.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class GeoCommuneDTO {

    private Integer pk;

    @NotNull
    @Size(max = 3)
    private String code;

    @NotNull
    @Size(max = 255)
    private String name;

    private Integer active;

    @NotNull
    private Integer fkPrefecture;

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

    public Integer getActive() {
        return active;
    }

    public void setActive(final Integer active) {
        this.active = active;
    }

    public Integer getFkPrefecture() {
        return fkPrefecture;
    }

    public void setFkPrefecture(final Integer fkPrefecture) {
        this.fkPrefecture = fkPrefecture;
    }

}
