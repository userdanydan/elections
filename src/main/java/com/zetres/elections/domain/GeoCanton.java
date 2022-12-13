package com.zetres.elections.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;


@Entity
public class GeoCanton implements ILieu{

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
    @JoinColumn(name = "fk_commune", nullable = false)
    private GeoCommune fkCommune;

    @OneToMany(mappedBy = "fkCanton")
    private Set<GeoLocalite> fkCantonGeoLocalites;

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
        return this.getFkCommune();
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

    public GeoCommune getFkCommune() {
        return fkCommune;
    }

    public void setFkCommune(final GeoCommune fkCommune) {
        this.fkCommune = fkCommune;
    }
    @JsonIgnore
    public Set<GeoLocalite> getFkCantonGeoLocalites() {
        return fkCantonGeoLocalites;
    }

    public void setFkCantonGeoLocalites(final Set<GeoLocalite> fkCantonGeoLocalites) {
        this.fkCantonGeoLocalites = fkCantonGeoLocalites;
    }

    @Override
    public String toString() {
        return "GeoCanton{" +
                "pk=" + pk +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", active=" + active +
                ", fkCommune=" + fkCommune +
                ", fkCantonGeoLocalites=" + fkCantonGeoLocalites +
                '}';
    }
}
