package co.arctern.api.provider.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonBackReference;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference("user-userRole")
    private User user;

    @ManyToOne
    @JsonBackReference("role-userRole")
    private Role role;

    @Column(columnDefinition = "tinyint(1) DEFAULT 1", nullable = false)
    private Boolean isActive;

    @Column(nullable = false, columnDefinition = "bigint(20) DEFAULT 1")
    @Version
    @JsonIgnore
    private Long version;

    public UserRole(Long version) {
        this.version = version;
    }

}
