package com.zetres.elections.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;


public class GeoLocaliteDTO {

    @Size(max = 50)
    private String pk;

    @NotNull
    @Size(max = 3)
    private String code;

    @NotNull
    @Size(max = 255)
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime dateAdded;

    private Integer active;

    @NotNull
    private Integer fkCanton;

    public String getPk() {
        return pk;
    }

    public void setPk(final String pk) {
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

    public OffsetDateTime getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(final OffsetDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(final Integer active) {
        this.active = active;
    }

    public Integer getFkCanton() {
        return fkCanton;
    }

    public void setFkCanton(final Integer fkCanton) {
        this.fkCanton = fkCanton;
    }

}
