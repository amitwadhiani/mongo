package co.arctern.api.provider.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonBackReference;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
//@Data
@NoArgsConstructor
public class UserTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @ManyToOne
    @JsonBackReference("user-userArea")
    @Getter
    @Setter
    private User user;

    @ManyToOne
    @JsonBackReference("area-userArea")
    @Getter
    @Setter
    private Task task;

    @Column(columnDefinition = "tinyint(1) DEFAULT 1", nullable = false)
    @Getter
    @Setter
    private Boolean isActive;

    @CreatedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Getter
    @Setter
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Getter
    @Setter
    private Timestamp lastModifiedAt;


    @Column(nullable = false, columnDefinition = "bigint(20) DEFAULT 1")
    @Getter
    @Setter
    @Version
    @JsonIgnore
    private Long version;

    public UserTask(Long version) {
        this.version = version;
    }


}
