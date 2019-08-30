package co.arctern.rider.api.domain;

import co.arctern.rider.api.util.EncodingUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
public class User extends EncodingUtil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ColumnDefault("tinyint(1) DEFAULT 1")
    private Boolean isActive;

    @Size(min = 4, max = 30, message = "Minimum username length : 4")
    private String username;

    @Email
    private String email;

    @Lob
    private String profilePic;

    @NotEmpty
    @Pattern(regexp = "(^$|[0-9]{10})")
    private String phone;

    private Timestamp lastLoginTime;

    @LastModifiedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp lastModified;

    @CreatedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 6, max = 30, message = "Minimum password length : 6")
    private String password;

    @ManyToMany
    private List<Role> roles;

    @OneToMany
    List<RideStateFlow> riderStateFlows;

    @OneToMany
    List<Area> areas;

    public void setPassword() {
        this.password = super.encodeString(password);
    }

}

