package co.arctern.api.provider.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ")
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp lastModifiedAt;

    @OneToOne(mappedBy = "taskEvent")
    private TaskEventFlow taskEventFlow;

    @ManyToOne
    @JsonBackReference("task-taskEvent")
    private Task task;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, columnDefinition = "bigint(20) DEFAULT 1")
    @Version
    private Long version;

    public Event(Long version) {
        this.version = version;
    }

}
