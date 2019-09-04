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
    private OTPState status;

    private String generatedOTP;
    private String contact;

    @Enumerated(EnumType.STRING)
    private UserState userState;

    private Boolean loginState;

    private Timestamp logoutTime;

    @ManyToOne
    @JsonBackReference("user-login")
    User user;

    @Column(nullable = false, columnDefinition = "bigint(20) DEFAULT 1")
    @Version
    @JsonIgnore
    private Long version;

    public Login(Long version) {
        this.version = version;
    }


}
