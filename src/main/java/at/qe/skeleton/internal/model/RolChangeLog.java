package at.qe.skeleton.internal.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class RolChangeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Userx user;
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<UserxRole> oldRoles = new ArrayList<>();
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<UserxRole> newRoles = new ArrayList<>();
    @Column(name = "change_time", nullable = false)
    private LocalDateTime chanegeTime;

    public Userx getUser() {
        return user;
    }

    public RolChangeLog setUser(Userx user) {
        this.user = user;
        return this;
    }

    public List<UserxRole> getOldRoles() {
        return oldRoles;
    }

    public RolChangeLog setOldRoles(List<UserxRole> oldRoles) {
        this.oldRoles = oldRoles;
        return this;
    }

    public List<UserxRole> getNewRoles() {
        return newRoles;
    }

    public RolChangeLog setNewRoles(List<UserxRole> newRoles) {
        this.newRoles = newRoles;
        return this;
    }

    public LocalDateTime getChanegeTime() {
        return chanegeTime;
    }

    public RolChangeLog setChanegeTime(LocalDateTime chanegeTime) {
        this.chanegeTime = chanegeTime;
        return this;
    }
}