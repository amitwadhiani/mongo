package co.arctern.api.provider.domain;

import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.constant.TaskType;
import co.arctern.api.provider.util.CodeGeneratorUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonBackReference;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Task extends CodeGeneratorUtil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    @Getter
    private String code;

    @CreatedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp lastModifiedAt;

    @Column(nullable = true)
    private Timestamp expectedArrivalTime;

    @Column(nullable = true)
    private String source;

    @OneToMany(mappedBy = "task")
    private List<TaskEvent> taskEvents;

    @OneToMany(mappedBy = "task")
    private List<Payment> payments;

    @Column
    private Long diagnosticOrderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'OPEN'")
    private TaskState state;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'SAMPLE_PICKUP'")
    private TaskType type;

    private Boolean isPrepaid;

    @OneToOne(mappedBy = "task")
    private Rating rating;

    private Long activeUserId;

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
    Address destinationAddress;

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
    private String patientId;

    @PrePersist
    public void setCode() {
        this.code = generateTaskCode(this.id, this.refId, this.type);
    }

    public Task(Long version) {
        this.version = version;
    }


}
