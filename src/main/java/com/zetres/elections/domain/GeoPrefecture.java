package com.zetres.elections.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;


@Entity
public class GeoPrefecture implements ILieu{

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
    private String shortName;

    @Column
    private Integer active;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_region", nullable = false)
    private GeoRegion fkRegion;
    @JsonIgnore
    @OneToMany(mappedBy = "fkPrefecture")
    private Set<GeoCommune> fkPrefectureGeoCommunes;

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
       return this.getFkRegion();
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

    public GeoRegion getFkRegion() {
        return fkRegion;
    }

    public void setFkRegion(final GeoRegion fkRegion) {
        this.fkRegion = fkRegion;
    }

    public Set<GeoCommune> getFkPrefectureGeoCommunes() {
        return fkPrefectureGeoCommunes;
    }

    public void setFkPrefectureGeoCommunes(final Set<GeoCommune> fkPrefectureGeoCommunes) {
        this.fkPrefectureGeoCommunes = fkPrefectureGeoCommunes;
    }

    @Override
    public String toString() {
        return "GeoPrefecture{" +
                "pk=" + pk +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", active=" + active +
                ", fkRegion=" + fkRegion +
                ", fkPrefectureGeoCommunes=" + fkPrefectureGeoCommunes +
                '}';
    }
}
