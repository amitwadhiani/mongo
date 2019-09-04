package co.arctern.api.provider.domain;

import co.arctern.api.provider.constant.TaskEventState;
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
public class TaskEvent {

    @Id
    private Long id;

    @CreatedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ")
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp lastModifiedAt;

    @ManyToOne
    @JsonBackReference("task-taskEvent")
    private Task task;

    @Enumerated(EnumType.STRING)
    private TaskEventState state;

    private Long userId;

    @Column(nullable = false, columnDefinition = "bigint(20) DEFAULT 1")
    @Version
    private Long version;

    public TaskEvent(Long version) {
        this.version = version;
    }

}
