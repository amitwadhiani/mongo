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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp lastModifiedAt;

    @Column(nullable = true)
    private Timestamp expectedArrivalTime;

    @OneToMany(mappedBy = "task")
    private List<TaskEvent> taskEvents;

    @OneToMany(mappedBy = "task")
    private List<Payment> payments;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'OPEN'")
    private TaskState state;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'SAMPLE_PICKUP'")
    private TaskType type;

    private Boolean isPrepaid;

    @OneToOne(mappedBy = "task")
    private Rating rating;

    /**
     * record of users working on/have worked upon a task.
     */
    @OneToMany(mappedBy = "task")
    private List<UserTask> userTasks;

    /**
     * record of status flows for a task.
     */
    @OneToMany(mappedBy = "task")
    private List<TaskStateFlow> taskStateFlows;

    /**
     * orderId linked with the item.
     */
    private Long refId;

    @ManyToOne
    @JsonBackReference("sourceAddress-task")
    Address sourceAddress;

    @ManyToOne
    @JsonBackReference("destinationAddress-task")
    Address destinationAddressId;

    @Column(columnDefinition = "tinyint(1) DEFAULT 1", nullable = false)
    private Boolean isActive;

    @Column(columnDefinition = "tinyint(1) DEFAULT 0", nullable = false)
    private Boolean cancellationRequested;

    @OneToMany(mappedBy = "task")
    private List<TaskReason> taskReasons;

    @Column
    private String patientName;

    @Column(nullable = false, columnDefinition = "bigint(20) DEFAULT 1")
    @Version
    @JsonIgnore
    private Long version;

    @NotEmpty
    @Pattern(regexp = "(^$|[0-9]{10})")
    @Column(nullable = false)
    private String patientPhone;

    @Column
    private String patientAge;

    public Task(Long version) {
        this.version = version;
    }


}
