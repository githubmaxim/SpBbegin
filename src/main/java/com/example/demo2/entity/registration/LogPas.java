package com.example.demo2.entity.registration;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

/**
 * Entity for storing customer registration information.
 *
 * @author Maxim
 * @version 1.0
 */

@Entity
@Table(name = "logpas")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LogPas {
    @Id
    @GeneratedValue
    private Integer id;

    /**
     * user name
     */
    private String username;

    /**
     * user password
     */
    private String password;

    /**
     * I don't know why this field is needed to register clients
     */
    private boolean active;

    /**
     * field of Creating table "logpass_role" and connection with her
     */
    //Ускоренное написание создания зависимости @OneToMany, без дополнительного создания сущности (а только класса
    // или интерфейса) и как следствие без возможности из класса/интерфейса продолжения связей в глубину
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "logpass_role", joinColumns = @JoinColumn(name = "logpass_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    public boolean getActive() { return active; }
    public void setActive(boolean active) {
        this.active = active;
    }
}
