package org.example.passwordgeneration.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "websites")
public class Website {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String websiteName;

    @OneToMany(mappedBy = "website")
    private Set<User> websites = new HashSet<>();
    public Website(String websiteName){
        this.websiteName = websiteName;
    }
}
