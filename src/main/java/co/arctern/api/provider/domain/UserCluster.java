package co.arctern.api.provider.domain;

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
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "cluster_id", "isActive"})})
public class UserCluster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference("user-userCluster")
    private User user;

    @ManyToOne
    @JsonBackReference("cluster-userCluster")
    private Cluster cluster;

    @LastModifiedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp lastModifiedAt;

    @CreatedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;


    @Column(columnDefinition = "tinyint(1) DEFAULT 1", nullable = false)
    private Boolean isActive;

    @Column(nullable = false, columnDefinition = "bigint(20) DEFAULT 1")
    @Version
    @JsonIgnore
    private Long version;

    public UserCluster(Long version) {
        this.version = version;
    }


}
