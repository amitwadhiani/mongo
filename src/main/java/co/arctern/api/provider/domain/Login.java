package co.arctern.api.provider.domain;

import co.arctern.api.provider.constant.OTPState;
import co.arctern.api.provider.constant.UserState;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonBackReference;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
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

    @Enumerated
    private OTPState otpState;

    private String generatedOTP;
    private String contact;
    private UserState userState;

    @ManyToOne
    @JsonBackReference("user-login")
    User user;

    @OneToMany(mappedBy = "login")
    List<LoginStateFlow> loginStateFlows;

}
