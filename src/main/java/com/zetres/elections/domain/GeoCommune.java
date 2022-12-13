package com.zetres.elections.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;


@Entity
public class GeoCommune implements ILieu{

    @Id
    @Column(nullable = false, updatable = false)
//    @SequenceGenerator(
//            name = "primary_sequence",
//            sequenceName = "primary_sequence",
//            allocationSize = 1,
//            initialValue = 10000
//    )
//    @GeneratedValue(
//            strategy = GenerationType.SEQUENCE,
//            generator = "primary_sequence"
//    )
    private Integer pk;

    @Column(nullable = false, length = 3)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column
    private Integer active;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_prefecture", nullable = false)
    private GeoPrefecture fkPrefecture;

    @OneToMany(mappedBy = "fkCommune")
    private Set<GeoCanton> fkCommuneGeoCantons;

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

    @Override
    @JsonIgnore
    public ILieu getSuperiorLieu() {
       return this.getFkPrefecture();
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

    public GeoPrefecture getFkPrefecture() {
        return fkPrefecture;
    }

    public void setFkPrefecture(final GeoPrefecture fkPrefecture) {
        this.fkPrefecture = fkPrefecture;
    }
    @JsonIgnore
    public Set<GeoCanton> getFkCommuneGeoCantons() {
        return fkCommuneGeoCantons;
    }

    public void setFkCommuneGeoCantons(final Set<GeoCanton> fkCommuneGeoCantons) {
        this.fkCommuneGeoCantons = fkCommuneGeoCantons;
    }

    @Override
    public String toString() {
        return "GeoCommune{" +
                "pk=" + pk +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", active=" + active +
                ", fkPrefecture=" + fkPrefecture +
                ", fkCommuneGeoCantons=" + fkCommuneGeoCantons +
                '}';
    }
}
