package co.arctern.api.provider.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonBackReference;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class TaskEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @org.codehaus.jackson.annotate.JsonBackReference("event-taskEvent")
    private Event event;

    @ManyToOne
    @JsonBackReference("task-taskEvent")
    private Task task;

    @Column(columnDefinition = "tinyint(1) DEFAULT 1", nullable = false)
    private Boolean isActive;

    @Column(nullable = false, columnDefinition = "bigint(20) DEFAULT 1")
    @Version
    @JsonIgnore
    private Long version;

    @OneToOne(mappedBy = "taskEvent")
    private TaskEventFlow taskEventFlow;

    public TaskEvent(Long version) {
        this.version = version;
    }

}
