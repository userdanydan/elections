package com.zetres.elections.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;


@Entity
public class GeoRegion  implements ILieu{

    @Id
    @Column(nullable = false, updatable = true)
    private Integer pk;

    @Column
    private String name;

    @Column
    private Integer active;

    @OneToMany(mappedBy = "fkRegion")
    @JsonIgnore
    private Set<GeoPrefecture> fkRegionGeoPrefectures;

    public Integer getPk() {
        return pk;
    }

    public void setPk(final Integer pk) {
        this.pk = pk;
    }

    public String getName() {
        return name;
    }

    @Override
    @JsonIgnore
    public ILieu getSuperiorLieu() {
       return null;
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

    @JsonIgnore
    public Set<GeoPrefecture> getFkRegionGeoPrefectures() {
        return fkRegionGeoPrefectures;
    }

    public void setFkRegionGeoPrefectures(final Set<GeoPrefecture> fkRegionGeoPrefectures) {
        this.fkRegionGeoPrefectures = fkRegionGeoPrefectures;
    }

    @Override
    public String toString() {
        return "GeoRegion{" +
                "pk=" + pk +
                ", name='" + name + '\'' +
                ", active=" + active +
                ", fkRegionGeoPrefectures=" + fkRegionGeoPrefectures +
                '}';
    }
}
