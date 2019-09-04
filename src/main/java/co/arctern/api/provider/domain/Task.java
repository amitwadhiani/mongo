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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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

    @Enumerated(EnumType.STRING)
    private TaskState state;

    @Enumerated(EnumType.STRING)
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

    @NotEmpty
    @Pattern(regexp = "(^$|[0-9]{10})")
    @NotNull(message = "Patient Phone mandatory")
    private String patientPhone;

    public Task(Long version) {
        this.version = version;
    }


}
