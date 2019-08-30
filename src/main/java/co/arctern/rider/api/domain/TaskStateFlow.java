package co.arctern.rider.api.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonBackReference;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;

@Entity
@Data
public class TaskStateFlow {

    @Id
    private Long id;

    @CreatedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp lastModifiedAt;

    @ManyToOne
    @JsonBackReference("user-riderStateFlow")
    private User user;

    @ManyToOne
    @JsonBackReference("task-taskStateFlow")
    private Task task;
}
