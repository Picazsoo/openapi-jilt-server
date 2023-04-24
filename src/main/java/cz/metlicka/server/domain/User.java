package cz.metlicka.server.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "middle_name", length = 50)
    private String middleName;

    @Column(name = "email")
    private String email;

    @Setter(AccessLevel.NONE)
    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Address address;

    public void setAddress(Address address) {
        this.address = address;
        this.address.setUser(this);
    }
}
