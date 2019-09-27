package co.arctern.api.provider.domain;

import co.arctern.api.provider.constant.OTPState;
import co.arctern.api.provider.constant.UserState;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonBackReference;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Login {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp lastModifiedAt;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'GENERATED' ")
    private OTPState status;

    @Column(columnDefinition = "TEXT")
    private String picture1;

    @Column(columnDefinition = "TEXT")
    private String picture2;

    @Column(nullable = false)
    private String generatedOTP;

    @Column(nullable = false)
    private String contact;

    @Enumerated(EnumType.STRING)
    private UserState userState;

    /**
     * login or logout
     */
    @Column(columnDefinition = "tinyint(1) DEFAULT 1")
    private Boolean loginState;

    @Column
    private Timestamp logoutTime;

    @ManyToOne
    @JsonBackReference("user-login")
    User user;

    @OneToMany(mappedBy = "login")
    private List<LoginFlow> loginFlows;

    @Column(nullable = false, columnDefinition = "bigint(20) DEFAULT 1")
    @Version
    @JsonIgnore
    private Long version;

    public Login(Long version) {
        this.version = version;
    }

}
