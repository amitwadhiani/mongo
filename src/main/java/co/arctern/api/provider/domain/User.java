package co.arctern.api.provider.domain;

import co.arctern.api.provider.util.EncodingUtil;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "phone")})
public class User extends EncodingUtil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Boolean isActive;

    private Boolean isTest;

    @Size(min = 4, max = 30, message = "Minimum username length : 4")
    @Column(unique = true)
    @NotNull(message = "Username mandatory")
    private String username;

    @Email
    @Column(unique = true)
    private String email;

    @Lob
    private String profilePic;

    @NotEmpty
    @Pattern(regexp = "(^$|[0-9]{10})")
    @NotNull(message = "Phone mandatory")
    private String phone;

    private Timestamp lastLoginTime;

    @LastModifiedDate
    private Timestamp lastModified;

    @CreatedDate
    private Timestamp createdAt;

    @Size(min = 6, max = 30, message = "Minimum password length : 6")
    @NotNull
    @NotEmpty
    private String password;

    @OneToMany(mappedBy = "user")
    List<Area> areas;

    @OneToMany(mappedBy = "user")
    List<Task> tasks;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    public void setPassword(String password) {
        this.password = super.encodeString(password);
    }

}

