package at.qe.skeleton.internal.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;
/**
 * Entity representing favorit Location of a user.
 *
 */

@Entity
public class FavLocation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Use GenerationType.AUTO for some databases
    private Long id;
    @Column(length = 100)
    private String name;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Userx user;
    private double longitude;
    private double latitude;
    /**
     * index = describes the value of the current position in the List
     */
    private Integer index;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Userx getUser() {
        return user;
    }
    public void setUser(Userx user) {
        this.user = user;
    }
    public Integer getIndex() {
        return index;
    }
    public void setIndex(Integer index) {
        this.index = index;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavLocation location = (FavLocation) o;
        return Double.compare(location.longitude, longitude) == 0 && Double.compare(location.latitude, latitude) == 0 && name.equals(location.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, longitude, latitude);
    }

    @Override
    public String toString() {
        return "at.qe.skeleton.model.Location{" + "name='" + name + '\'' + ", index=" + index + '}';
    }

}
