package com.springbank.security;

import com.springbank.users.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table
public class Credentials {

    @Id
    private String nId;

    @Column
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id", referencedColumnName = "ID")
    private User user;

    @ElementCollection(targetClass = Role.class ,fetch = FetchType.EAGER)
    @CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "nId"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<Role> role;

    @Column(nullable = false)
    @ColumnDefault("true")
    private boolean enabled;
}
