package com.zetres.elections.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;


@Entity
@Audited
public class GeoLocalite implements ILieu {

    @Id
    @Column(nullable = false, updatable = false, length = 50)
    private String pk;

    @Column(nullable = false, length = 3)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column
    private OffsetDateTime dateAdded;

    @Column
    private Integer active;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_canton", nullable = false)
    @NotAudited
    private GeoCanton fkCanton;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "fkLocalite")
    private Set<GeoCentre> fkLocaliteGeoCentres;

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

    @Override
    @JsonIgnore
    public ILieu getSuperiorLieu() {
       return this.getFkCanton();
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

    public GeoCanton getFkCanton() {
        return fkCanton;
    }

    public void setFkCanton(final GeoCanton fkCanton) {
        this.fkCanton = fkCanton;
    }
    @JsonIgnore
    public Set<GeoCentre> getFkLocaliteGeoCentres() {
        return fkLocaliteGeoCentres;
    }

    public void setFkLocaliteGeoCentres(final Set<GeoCentre> fkLocaliteGeoCentres) {
        this.fkLocaliteGeoCentres = fkLocaliteGeoCentres;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeoLocalite that = (GeoLocalite) o;
        return pk.equals(that.pk) && code.equals(that.code) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pk, code, name);
    }

    @Override
    public String toString() {
        return "GeoLocalite{" +
                "pk='" + pk + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", dateAdded=" + dateAdded +
                ", active=" + active +
                ", fkCanton=" + fkCanton +
                ", fkLocaliteGeoCentres=" + fkLocaliteGeoCentres +
                '}';
    }
}
