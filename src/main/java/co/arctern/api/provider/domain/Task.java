package co.arctern.api.provider.domain;

import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.constant.TaskType;
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
public class Task {

    @Id
    private Long id;

    @CreatedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp lastModifiedAt;

    @OneToMany(mappedBy = "task")
    private List<TaskEvent> taskEvents;

    private TaskState state;

    private TaskType type;

    private Boolean isPrepaid;

    @OneToOne(mappedBy = "task")
    private Rating rating;

    @OneToMany(mappedBy = "task")
    private List<UserTask> userTasks;

    private Long diagnosticOrderId;

    private Float amount;

    private String paymentState;

    @ManyToOne
    @JsonBackReference("address-task")
    Address address;

    @Column(columnDefinition = "tinyint(1) DEFAULT 1", nullable = false)
    private Boolean isActive;

    private Boolean cancellationRequested;

    @Column(nullable = false, columnDefinition = "bigint(20) DEFAULT 1")
    @Version
    @JsonIgnore
    private Long version;

    public Task(Long version) {
        this.version = version;
    }


}
