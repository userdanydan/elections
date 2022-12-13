package com.zetres.elections.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayDeque;
import java.util.Objects;
import java.util.StringJoiner;


@Entity
@Audited
public class GeoCentre implements ILieu {

    @Id
    @Column(nullable = false, updatable = false, length = 50)
    private String pk;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 20)
    private String kitidbegin;

    @Column(length = 20)
    private String fullkitidbegin;

    @Column
    private OffsetDateTime dateAdded;

    @Column
    private Integer active;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_localite", nullable = false)
    @Audited(withModifiedFlag = true)
    private GeoLocalite fkLocalite;

    public String getPk() {
        return pk;
    }

    public void setPk(final String pk) {
        this.pk = pk;
    }

    public String getName() {
        return name;
    }

    @Override
    @JsonIgnore
    public ILieu getSuperiorLieu() {
        return this.getFkLocalite();
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

    public GeoLocalite getFkLocalite() {
        return fkLocalite;
    }

    public void setFkLocalite(final GeoLocalite fkLocalite) {
        this.fkLocalite = fkLocalite;
    }

    @Override
    public String toString() {
        return "GeoCentre{" +
                "pk='" + pk + '\'' +
                ", name='" + name + '\'' +
                ", kitidbegin='" + kitidbegin + '\'' +
                ", fullkitidbegin='" + fullkitidbegin + '\'' +
                ", dateAdded=" + dateAdded +
                ", active=" + active +
                ", fkLocalite=" + fkLocalite +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeoCentre geoCentre = (GeoCentre) o;
        return Objects.equals(pk, geoCentre.pk) && Objects.equals(name, geoCentre.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pk, name);
    }

    @JsonIgnore
    @Column(name="fk_localite")
    public String getForeignKey(String name){
        return name;
    }
    @JsonIgnore
    public String getGeoHierarchy() {
        ILieu lieuSup = this;
        var stack = new ArrayDeque<String>();
        var sj = new StringJoiner(" > ");
        do{
            stack.push(lieuSup.getName());
            lieuSup = lieuSup.getSuperiorLieu();
        }while(lieuSup!=null);
        while(!stack.isEmpty()){
            sj.add(stack.pop());
        }
        return sj.toString();
    }
}
