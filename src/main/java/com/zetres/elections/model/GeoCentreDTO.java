package com.zetres.elections.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;


public class GeoCentreDTO {

    @Size(max = 50)
    private String pk;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 20)
    private String kitidbegin;

    @Size(max = 20)
    private String fullkitidbegin;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime dateAdded;

    private Integer active;

    @NotNull
    @Size(max = 50)
    private String fkLocalite;

    public String getPk() {
        return pk;
    }

    public void setPk(final String pk) {
        this.pk = pk;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getKitidbegin() {
        return kitidbegin;
    }

    public void setKitidbegin(final String kitidbegin) {
        this.kitidbegin = kitidbegin;
    }

    public String getFullkitidbegin() {
        return fullkitidbegin;
    }

    public void setFullkitidbegin(final String fullkitidbegin) {
        this.fullkitidbegin = fullkitidbegin;
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

    public String getFkLocalite() {
        return fkLocalite;
    }

    public void setFkLocalite(final String fkLocalite) {
        this.fkLocalite = fkLocalite;
    }

    @Override
    public String toString() {
        return "GeoCentreDTO{" +
                "pk='" + pk + '\'' +
                ", name='" + name + '\'' +
                ", kitidbegin='" + kitidbegin + '\'' +
                ", fullkitidbegin='" + fullkitidbegin + '\'' +
                ", dateAdded=" + dateAdded +
                ", active=" + active +
                ", fkLocalite='" + fkLocalite + '\'' +
                '}';
    }
}
