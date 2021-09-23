package com.example.demo2.entity.registration;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

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
    private String username;
    private String password;
    private boolean active;

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
