package co.arctern.api.provider.domain;

import co.arctern.api.provider.constant.UserType;
import com.amazonaws.services.polly.model.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "phone")})
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "tinyint(1) DEFAULT 1", nullable = false)
    private Boolean isActive;

    @Column(columnDefinition = "tinyint(1) DEFAULT 0", nullable = false)
    private Boolean isTest;

    @Column(columnDefinition = "tinyint(1) DEFAULT 0", nullable = false)
    private Boolean loginState;

    private UserType type;

    @LastModifiedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp lastModified;

    @CreatedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp createdAt;


    @Size(min = 3, max = 30, message = "Minimum username length : 3")
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
    @Column(unique = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Integer age;

    private Timestamp lastLoginTime;

    @NotNull
    @NotEmpty
    @Lob
    private String password;

    @OneToMany(mappedBy = "user")
    List<UserArea> userAreas;

    @OneToMany(mappedBy = "user")
    private List<UserTask> userTasks;

    @OneToMany(mappedBy = "user")
    private List<UserRole> userRoles;

    @Column(nullable = false, columnDefinition = "bigint(20) DEFAULT 1")
    @Version
    @JsonIgnore
    private Long version;

    public User(Long version) {
        this.version = version;
    }


}

