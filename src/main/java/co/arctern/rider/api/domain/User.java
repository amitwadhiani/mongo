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
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "phone")})
public class User extends EncodingUtil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Boolean isActive;

    private Boolean isTest;

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
    private Timestamp lastModified;

    @CreatedDate
    private Timestamp createdAt;

    @Size(min = 6, max = 30, message = "Minimum password length : 6")
    private String password;

    @OneToMany(mappedBy = "user")
    List<Area> areas;

    private List<String> roles;

    public void setPassword(String password) {
        this.password = super.encodeString(password);
    }

}

